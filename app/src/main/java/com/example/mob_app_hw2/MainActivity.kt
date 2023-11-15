package com.example.mob_app_hw2

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }

    @Composable
    fun MyApp() {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "welcome"
        ) {
            composable("welcome") {
                WelcomeScreen(navController)
            }
            composable("cityList") {
                CityListScreen(navController)
            }
            composable("cityDetails/{city}") { // Make sure the route includes the parameter
                    backStackEntry ->
                val city = backStackEntry.arguments?.getString("city")
                val viewModelFactory = WeatherApiViewModelFactory(city = city.toString())
                val viewModel = ViewModelProvider(ViewModelStore(), viewModelFactory)[WeatherApiViewModel::class.java]
                if (city != null) {
                    CityDetailsScreen(city = city, navController, viewModel)
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    var cityName by remember { mutableStateOf("") }
    val fusedLocationClient: FusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }


    // Use the requestPermission contract to request location permission
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                println("inside granted")
            }else {
                // Permission denied, handle accordingly
                // TODO: Add your logic for denied permission
            }
        }

//    // Request location permission on app startup
    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    // Use reverse geocoding to get the city name from the location
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )

                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            cityName = addresses[0].locality
                            // Do something with the city name
                        }
                    }
                }
                // Permission granted, you can now access the user's location
                // TODO: Add your location-related logic here
            }
            // Permission already granted, you can now access the user's location
            // TODO: Add your location-related logic here
        } else {
            // Request location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Dispose the effect to run only once on app startup
        onDispose { }
    }

    val viewModelFactory = WeatherApiViewModelFactory(city = cityName)
    val viewModel = ViewModelProvider(ViewModelStore(), viewModelFactory)[WeatherApiViewModel::class.java]
    val weatherInfo by viewModel.weatherInfo.observeAsState()
    val temp = weatherInfo?.current?.temp_c

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Your App",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Get started by clicking the button below!",
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("cityList")
            }
        ) {
            Text(text = "Get Started")
        }
        Text(
            text = "Temperature: $temp °C",
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun CityListScreen(navController: NavHostController) {

    val cities = listOf(
        "Yerevan",
        "Washington",
        "Madrid",
        "Moscow",
        "Lisbon",
        "London",
        "Berlin",

        // Add more cities here
    )

    LazyColumn {
        items(cities) { city ->
            CityItem(city, navController)
        }
    }

    IconButton(
        onClick = {
            navController.navigate("welcome")
        }
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back to Welcome",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CityItem(city: String, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("cityDetails/$city")
            }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .offset(y = 30.dp) // Add a vertical offset to move the city item down
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "City",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = city,
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}



val cityImageMap = mapOf(
    "Yerevan" to R.drawable.yerevan_image,
    "Washington" to R.drawable.washington_image,
    "Madrid" to R.drawable.madrid_image,
    "Moscow" to R.drawable.moscow_image,
    "Lisbon" to R.drawable.lisbon_image,
    "London" to R.drawable.london_image,
    "Berlin" to R.drawable.berlin_image,

    // Add more city-image mappings as needed
)

val cityDescriptionMap = mapOf(
    "Yerevan" to "Yerevan is the capital and largest city of Armenia and one of the world's oldest continuously inhabited cities. Situated along the Hrazdan River, Yerevan is the administrative, cultural, and industrial center of the country, as its primate city. It has been the capital since 1918, the fourteenth in the history of Armenia and the seventh located in or around the Ararat Plain. The city also serves as the seat of the Araratian Pontifical Diocese, which is the largest diocese of the Armenian Apostolic Church and one of the oldest dioceses in the world.",
    "Washington" to "Washington, D.C., formally the District of Columbia and commonly called Washington or D.C., is the capital city and the federal district of the United States. The city is located on the east bank of the Potomac River, which forms its southwestern border with Virginia and borders Maryland to its north and east. Washington, D.C. was named for George Washington, a Founding Father, victorious commanding general of the Continental Army in the American Revolutionary War, and the first president of the United States who is widely considered the \"Father of his country\". The district is named for Columbia, the female personification of the nation.",
    "Madrid" to "Madrid is the capital and most populous city of Spain. The city has almost 3.4 million inhabitants and a metropolitan area population of approximately 6.7 million. It is the second-largest city in the European Union (EU), and its monocentric metropolitan area is the second-largest in the EU. The municipality covers 604.3 km2 (233.3 sq mi) geographical area. Madrid lies on the River Manzanares in the central part of the Iberian Peninsula at about 650 metres above mean sea level. Capital city of both Spain and the surrounding autonomous community of Madrid (since 1983), it is also the political, economic and cultural centre of the country. The climate of Madrid features hot summers and cool winters.",
    "Moscow" to "Moscow is the capital and largest city of Russia. The city stands on the Moskva River in Central Russia, with a population estimated at 13.0 million residents within the city limits, over 18.8 million residents in the urban area, and over 21.5 million residents in the metropolitan area. The city covers an area of 2,511 square kilometers (970 sq mi), while the urban area covers 5,891 square kilometers (2,275 sq mi), and the metropolitan area covers over 26,000 square kilometers (10,000 sq mi). Moscow is among the world's largest cities, being the most populous city entirely in Europe, the largest urban and metropolitan area in Europe, and the largest city by land area on the European continent.",
    "Lisbon" to "Lisbon is the capital and largest city of Portugal, with an estimated population of 548,703 within its administrative limits in an area of 100.05 km2.\n" + "\n" + "About 2.9 million people live in the Lisbon metropolitan area, which extends beyond the city's administrative area, making it the third largest metropolitan area in the Iberian Peninsula, after Madrid and Barcelona as well as the 11th-most populous urban area in the European Union. It represents approximately 27.7% of the country's population.\n" + "\n" + "Lisbon is mainland Europe's westernmost capital city (second overall after Reykjavik) and the only one along the Atlantic coast, the others (Reykjavik and Dublin) being on islands.\n",
    "London" to "London is the capital and largest city of England and the United Kingdom, with a population of around 8.8 million. It stands on the River Thames in south-east England at the head of a 50-mile (80 km) estuary down to the North Sea and has been a major settlement for nearly two millennia. The City of London, its ancient core and financial centre, was founded by the Romans as Londinium and retains its medieval boundaries. The City of Westminster, to the west of the City of London, has for centuries hosted the national government and parliament.",
    "Berlin" to "Berlin is the capital and largest city of Germany by both area and population. Its more than 3.85 million inhabitants make it the European Union's most populous city, according to population within city limits. One of Germany's sixteen constituent states, Berlin is surrounded by the State of Brandenburg and contiguous with Potsdam, Brandenburg's capital. Berlin's urban area, which has a population of around 4.5 million, is the most populous urban area in Germany. The Berlin-Brandenburg capital region has around 6.2 million inhabitants and is Germany's second-largest metropolitan region after the Rhine-Ruhr region.",

    // Add more city-image mappings as needed
)

@Composable
fun CityDetailsScreen(city: String, navController: NavHostController, viewModel: WeatherApiViewModel) {
    // For simplicity, we'll display a static description and image for demonstration purposes.
    val imageResource = cityImageMap[city]
    val cityDescription = cityDescriptionMap[city]
    val weatherInfo by viewModel.weatherInfo.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.navigate("cityList")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to City List",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Welcome to $city",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (imageResource != null) {
            val imagePainter = painterResource(id = imageResource)
            Image(
                painter = imagePainter,
                contentDescription = "City Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cityDescription != null) {
            Text(
                text = cityDescription,
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            Text(
                text = "This is a great city with lots to see and do",
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (weatherInfo != null) {
            val temp = weatherInfo!!.current.temp_c
            Text(
                text = "Temperature: $temp °C",
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                )
        }else{
            Text(
                text = "Temperature not defined",
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        BackHandler {
            navController.popBackStack()
        }
    }
}

class WeatherApiViewModel(private val city: String) : ViewModel() {
    private val weatherApiService = RetrofitClient.weatherApiService

    private val _weatherInfo = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo> get() = _weatherInfo

    init {
        viewModelScope.launch {
            try {
                val response = weatherApiService.getWeatherInfo(city)

                _weatherInfo.value = response
            } catch (e: Exception) {
                Log.e("Retrofit", "Error fetching user data", e)
            }
        }
    }
}

class WeatherApiViewModelFactory(private val city: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherApiViewModel::class.java)) {
            return WeatherApiViewModel(city) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

object RetrofitClient {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val API_KEY = "c6a6b640146a4cc59f281844231211"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val requestBuilder = original.newBuilder()
                            .header("key", API_KEY)
                        val request = requestBuilder.build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
    }

    val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}

data class WeatherInfo(
    val current: CurrentWeather,
)

data class CurrentWeather(
    val temp_c: Double,
)

interface WeatherApiService {
    @GET("current.json")
    suspend fun getWeatherInfo(@Query("q") city: String): WeatherInfo
}
