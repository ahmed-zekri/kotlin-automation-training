package data.dto

import com.google.gson.annotations.SerializedName

data class Car(
    @SerializedName("Year")
    val year: Int,
    @SerializedName("Make")
    val make: String,
    @SerializedName("Model")
    val model: String,
    @SerializedName("Category")
    val category: String
)
