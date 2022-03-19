/*
 * dev.kobalt.csv2i18n
 * Copyright (C) 2022 Tom.K
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.kobalt.csv2i18n.web.convert

import dev.kobalt.csv2i18n.web.resource.ResourceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withContext
import java.io.*

object ConvertRepository {

    val pageTitle = "Convert"
    val pageSubtitle = "Upload your content in this form to convert it."
    val pageRoute = "convert/"
    val pageContent = ResourceRepository.getText("convert.md")!!

    var jarPath: String? = null

    private val semaphore = Semaphore(1)

    suspend fun Semaphore.acquireUse(method: () -> Unit) {
        try {
            acquire()
            method()
        } catch (e: Throwable) {
            throw e
        } finally {
            release()
        }
    }

    suspend fun submit(data: String, outputStream: OutputStream): OutputStream {
        return withContext(Dispatchers.IO) {
            semaphore.acquireUse {
                val process = ProcessBuilder(
                    "java",
                    "-jar", jarPath!!
                ).start()
                BufferedWriter(OutputStreamWriter(process.outputStream)).use { stdin ->
                    stdin.write(data); stdin.flush()
                }
                BufferedInputStream(process.inputStream).use { stdout ->
                    outputStream.write(stdout.readBytes())
                }
                var s: String?
                BufferedReader(InputStreamReader(process.errorStream)).use { stderr ->
                    while (stderr.readLine().also { s = it } != null) println(s)
                }
            }
            outputStream
        }
    }

}