package controllers

import java.io.File

import org.specs2.mutable.Specification
import play.api.libs.json.JsArray

/**
  * Created by zongh on 10/26/2016.
  */
class ApplicationTest extends Specification {

  "application" should {

    val cities = Application.loadCity(new File("test/resources/data/city.sample.json"))

    "load the city data from a file" in {
      cities.size must_== 1006
    }

    "calculate centroid" in {
      math.abs((cities.apply(0) \ "centroidLongitude").as[Double] - (-176.6287565)) must be <= 0.00001
      math.abs((cities.apply(0) \ "centroidLatitude").as[Double] - 51.8209920) must be <= 0.00001
    }

    "find cities whose centroids are in the current region" in {
      val result = Application.findCity(35, 33, -85, -87, cities)
      val features = (result \ "features").as[JsArray]
      val cityIDs = List.newBuilder[Double]
      for (city <- features.value){
        cityIDs += (city \ "properties" \ "cityID").as[Double]
      }
      cityIDs.result().contains(100820) must_== true
    }
  }
}
