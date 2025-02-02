package com.ilustris.alicia.ai.inputs

interface InputGenerator {
    suspend fun generateInputs(): List<String>
}
