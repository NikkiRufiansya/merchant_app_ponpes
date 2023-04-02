package com.academica.tenant.core.helper


import android.content.Context
import android.content.res.Resources
import android.text.Editable
import com.academica.tenant.R


import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object Currency {
    fun nominal(number: Double): String {
        val numberFormat = DecimalFormat("#,###")

        return numberFormat.format(number).toString()
    }


    fun nominal(number: Editable): String {
        val numberFormat = DecimalFormat("#,###")

        return numberFormat.format(number).toString()
    }

    fun nominal(number: Int): String {
        val numberFormat = DecimalFormat("#,###")

        return numberFormat.format(number).toString()
    }

    fun nominal(number: String): String {
        val numberFormat = DecimalFormat("#,###")

        return numberFormat.format(number.replace(".", "").toDouble().toInt()).toString()
    }

    fun nominalRP(ctx: Context, number: Double): String {
        val numberFormat = DecimalFormat("#,###")

        return ctx.getString(R.string.rp, numberFormat.format(number).toString())
    }

    fun nominalRP(ctx: Context, number: Int): String {
        val numberFormat = DecimalFormat("#,###")

        return ctx.getString(R.string.rp, numberFormat.format(number).toString())
    }

    fun nominalRP(ctx: Context, number: String): String {
        val numberFormat = DecimalFormat("#,###")

        return ctx.getString(R.string.rp,
            numberFormat.format(number.replace(".", "").toDouble().toInt()).toString())
    }

    fun rp(ctx: Context, number: String): String {
        return ctx.getString(R.string.rp, number)

    }
}