package com.quan.lam.nytimesmovies.ui.filter

class MPAARatingItem(var mpaa_rating: String) {
    companion object {
        fun getItems() : List<MPAARatingItem>{
            val result = ArrayList<MPAARatingItem>()
            result.add(MPAARatingItem("G"))
            result.add(MPAARatingItem("PG"))
            result.add(MPAARatingItem("PG-13"))
            result.add(MPAARatingItem("R"))
            result.add(MPAARatingItem("NC-17"))
            return result
        }
    }
}