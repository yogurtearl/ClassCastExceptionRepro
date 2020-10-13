package com.example.classcastexceptionrepro
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent =  Intent().putExtra("Foo", Foo("Bar"))
        val foo = intent.getExtraParcelableOrNull<Foo>("Foo")
        println(foo)
    }
}

data class Foo(val bar: String?): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<Foo> {
        override fun createFromParcel(parcel: Parcel): Foo {
            return Foo(parcel)
        }

        override fun newArray(size: Int): Array<Foo?> {
            return arrayOfNulls(size)
        }
    }

}


fun <R : Parcelable> Intent.getExtraParcelableOrNull(key: String): R? =
        extras?.run {
            when {
                containsKey(key) -> getParcelableExtra(key)
                else -> null
            }
        }