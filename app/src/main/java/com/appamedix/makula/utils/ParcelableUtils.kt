package com.appamedix.makula.utils

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * Interface for classes whose instances can be written to and restored from a {@link Parcel}.
 * Classes implementing the Parcelable interface must also have a non-null static field called CREATOR
 * of a type that implements the {@link Parcelable.Creator} interface.
 */
interface KParcelable : Parcelable {
    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel, flags: Int)
}

// Creator factory functions

/**
 * Create a new instance of the Parcelable class.
 */
inline fun <reified T> parcelableCreator(
        crossinline create: (Parcel) -> T) =
        object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel) = create(source)
            override fun newArray(size: Int) = arrayOfNulls<T>(size)
        }

/**
 * Specialization of {@link Creator} that allows you to receive the
 * ClassLoader the object is being created in.
 */
inline fun <reified T> parcelableClassLoaderCreator(
        crossinline create: (Parcel, ClassLoader) -> T) =
        object : Parcelable.ClassLoaderCreator<T> {
            override fun createFromParcel(source: Parcel, loader: ClassLoader) = create(source, loader)
            override fun createFromParcel(source: Parcel) = createFromParcel(source, T::class.java.classLoader!!)
            override fun newArray(size: Int) = arrayOfNulls<T>(size)
        }

/// Parcel extensions

/**
 * Read a boolean value from parcelable object.
 *
 * @return A boolean value.
 */
inline fun Parcel.readBoolean() = readInt() != 0

/**
 * Write a boolean value to parcelable object.
 *
 * @param value: A boolean value to write.
 */
inline fun Parcel.writeBoolean(value: Boolean) = writeInt(if (value) 1 else 0)

/**
 * Read an enum value from parcelable object.
 *
 * @return An enum value with any type.
 */
inline fun <reified T : Enum<T>> Parcel.readEnum() =
        readInt().let { if (it >= 0) enumValues<T>()[it] else null }

/**
 * Write an enum value to parcelable object.
 *
 * @param value: An enum value with any type.
 */
inline fun <T : Enum<T>> Parcel.writeEnum(value: T?) =
        writeInt(value?.ordinal ?: -1)

/**
 * Read an optional value from parcelable object.
 *
 * @return A value if it's not null, otherwise null.
 */
inline fun <T> Parcel.readNullable(reader: () -> T) =
        if (readInt() != 0) reader() else null

/**
 * Write an optional value to parcelable object.
 *
 * @param value: Any type of optional value.
 */
inline fun <T> Parcel.writeNullable(value: T?, writer: (T) -> Unit) {
    if (value != null) {
        writeInt(1)
        writer(value)
    } else {
        writeInt(0)
    }
}

/**
 * Read a date date from parcelable object.
 *
 * @return An optional date object.
 */
fun Parcel.readDate() = readNullable { Date(readLong()) }

/**
 * Write an optional date object to parcelable object.
 *
 * @param value: An optional date object.
 */
fun Parcel.writeDate(value: Date?) = writeNullable(value) { writeLong(it.time) }

/**
 * Read a big integer (larger than long) as byte array from parcelable object.
 *
 * @return A optional big integer value.
 */
fun Parcel.readBigInteger() = readNullable { BigInteger(createByteArray()) }

/**
 * Write a big integer (larger than long) as byte array to parcelable object.
 *
 * @param value: An optional big integer value.
 */
fun Parcel.writeBigInteger(value: BigInteger?) =
        writeNullable(value) { writeByteArray(it.toByteArray()) }

/**
 * Read a decimal value as byte array from parcelable object.
 *
 * @return An optional decimal value.
 */
fun Parcel.readDecimal() =
        readNullable { BigDecimal(BigInteger(createByteArray()), readInt()) }

/**
 * Write a decimal value as byte array to parcelable object.
 *
 * @param value: An optional decimal value.
 */
fun Parcel.writeDecimal(value: BigDecimal?) = writeNullable(value) {
    writeByteArray(it.unscaledValue().toByteArray())
    writeInt(it.scale())
}

fun <T : Parcelable> Parcel.readTypedObjectCompat(c: Parcelable.Creator<T>) =
        readNullable { c.createFromParcel(this) }

fun <T : Parcelable> Parcel.writeTypedObjectCompat(value: T?, parcelableFlags: Int) =
        writeNullable(value) { it.writeToParcel(this, parcelableFlags) }