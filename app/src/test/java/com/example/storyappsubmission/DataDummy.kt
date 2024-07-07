package com.example.storyappsubmission

import com.example.storyappsubmission.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "urlPhoto + $i",
                "createAt + $i",
                "name $i",
                "decription $i",
                i.toDouble(),
                "id $i.to",
                i.toDouble()
            )

            items.add(story)
        }

        return items
    }
}
