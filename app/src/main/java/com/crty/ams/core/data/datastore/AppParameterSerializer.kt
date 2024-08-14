package com.crty.ams.core.data.datastore

import com.crty.ams.AppParameter
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AppParameterSerializer : Serializer<AppParameter> {
    override val defaultValue: AppParameter = AppParameter.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppParameter {
        try {
            return AppParameter.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AppParameter, output: OutputStream) = t.writeTo(output)
}
