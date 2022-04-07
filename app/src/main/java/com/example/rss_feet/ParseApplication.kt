package com.example.rss_feet
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory


class ParseApplication {
    private val TAG = "ParseApplications"
    val applications = ArrayList<FeedEntry>()

    fun parse( xmlData: String ): Boolean {
        var status = true
        var tagInEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val pullParser = factory.newPullParser()
            //https://developer.android.com/reference/org/xmlpull/v1/XmlPullParser

            pullParser.setInput(xmlData.reader())
            var eventType = pullParser.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = pullParser.name?.lowercase()
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: Starting tag for: $tagName")
                        if(tagName == "item") {
                            tagInEntry = true
                        }
                    }
                    XmlPullParser.TEXT -> {
                        textValue = pullParser.text
                    }
                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: Ending tag for: $tagName")
                        if(tagInEntry) {
                            when(tagName) {
                                "item" -> {
                                    applications.add(currentRecord)
                                    tagInEntry = false
                                    currentRecord = FeedEntry()
                                }
                                "title" -> currentRecord.name = textValue
                                "author" -> currentRecord.artist = textValue
                                "description" -> currentRecord.summary = textValue
                                "pubDate" -> currentRecord.releaseDate = textValue
                                "description" -> currentRecord.imageUrl = textValue
                            }
                        }
                    }
                }
                eventType = pullParser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return true
    }
}
