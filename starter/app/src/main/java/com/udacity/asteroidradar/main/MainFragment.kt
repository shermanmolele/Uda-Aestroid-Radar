package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.ResourceBoundUI
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.utils.Status

class MainFragment : Fragment(), ResourceBoundUI<List<Asteroid>> {
    companion object {
        const val TAG = "Main"
    }

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(activity?.applicationContext)
    }
    private lateinit var binding: FragmentMainBinding
    private val asteroidFeed: MutableList<Asteroid> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFeed()
        observePictureOfDay()
        observeViewModel()

        binding.mainError.setOnClickListener {
            viewModel.getFeed()
        }

        binding.asteroidRecycler.adapter =
            AsteroidFeedAdapter(context, AestroidListener { aestroidId ->
                viewModel.onAsteroidClicked(aestroidId)
            }, asteroidFeed)


        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(
                        MainFragmentDirections.actionShowDetail(asteroid)
                )
            }
        })
    }

    override fun observeViewModel() {
        viewModel.asteroidFeed.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    if (resource.data == null) {
                        empty()
                        return@Observer
                    }
                    idle()

                    bindViewModelData(resource.data)
                }
                Status.LOADING -> loading()
                Status.ERROR -> error()
            }
        })
    }

    override fun bindViewModelData(data: List<Asteroid>) {
        asteroidFeed.clear()
        asteroidFeed.addAll(data)
        binding.asteroidRecycler.adapter?.notifyDataSetChanged()
    }

    private fun observePictureOfDay() {
        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    if (resource.data == null) {
                        empty()
                        return@Observer
                    }
                    idle()

                    bindPictureOfDay(resource.data)
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                }
            }
        })
    }

    private fun bindPictureOfDay(data: PictureOfDay) {
        binding.activityMainImageOfTheDay.contentDescription = getString(
            R.string.nasa_picture_of_day_content_description_format,
            data.title
        )

        // binding.mainPicDayTitle.text = data.title

        Picasso.with(context)
            .load(data.url)
            .into(binding.activityMainImageOfTheDay)
    }

    override fun loading() {
        binding.mainEmpty.visibility = View.GONE
        binding.mainError.visibility = View.GONE
        binding.asteroidRecycler.visibility = View.GONE

        binding.statusLoadingWheel.visibility = View.VISIBLE
    }

    override fun idle() {
        binding.mainEmpty.visibility = View.GONE
        binding.mainError.visibility = View.GONE
        binding.statusLoadingWheel.visibility = View.GONE

        binding.asteroidRecycler.visibility = View.VISIBLE
    }

    override fun empty() {
        binding.statusLoadingWheel.visibility = View.GONE
        binding.mainError.visibility = View.GONE
        binding.asteroidRecycler.visibility = View.GONE

        binding.mainEmpty.visibility = View.VISIBLE
    }

    override fun error() {
        binding.mainEmpty.visibility = View.GONE
        binding.statusLoadingWheel.visibility = View.GONE
        binding.asteroidRecycler.visibility = View.GONE

        binding.mainError.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
