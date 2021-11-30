package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, item: Asteroid?) {
    if (item?.isPotentiallyHazardous == true) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, item: Asteroid?) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), item?.absoluteMagnitude)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView,  item: Asteroid?) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), item?.estimatedDiameter)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView,  item: Asteroid?) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), item?.relativeVelocity)
}

@BindingAdapter("distanceFromEarth")
fun bindTextViewToDisplayDistance(textView: TextView,  item: Asteroid?) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), item?.distanceFromEarth)
}

@BindingAdapter("approachDate")
fun bindTextViewToDisplayDate(textView: TextView,  item: Asteroid?) {
    val context = textView.context
    textView.text = item?.closeApproachDate
}
