# whitbreaddemo

I have implemented this project by connecting to the Foursquare API using HTTP calls.
I have used the Volley library for the HTTP calls, for the reason that I find it simpler to implement (creating custom calls)
and also easier to understand.
I have also used the Glide image loading library because it is more optimised than other libraries, like Picasso, without any configuration. It has optimisations like using the RGB 565 format which consumes less memory for images without transparency. It also caches the downloaded images in different sizes which allows them to be loaded quicker, since no resizing will be done while the image is being loaded during runtime.

For the parsing of data, I have used the Gson library which deserialises Json responses directly into Model classes.
The creation of the Model classes was done using the website http://www.jsonschema2pojo.org/.

The app requests the last known location of the device, in order to be able to make an API call for searching venues in the user's location. For this I've created a LocationActivity which contains all the necessary code for retrieving the device's location, which is extended by the MainActivity.
MainActivity handles all the callbacks about the location, user clicks and API responses.
The user is able to search using the SearchView widget, which also includes the history of past searches.
The activity is also retaining its data in cases where the screen orientation is changed.

For the display of data, I have used the RecyclerView.
Each venue displayes in the list, has an image of it's category handled by Glide, the venue name, it's distance from the user and also buttons to access more information about the venue like, it's Twitter page, map navigation, menu, etc.
