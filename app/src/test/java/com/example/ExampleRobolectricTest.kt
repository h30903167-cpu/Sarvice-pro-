package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ServiceProRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ServicePro", appName)
  }

  @Test
  fun `verify worker pricing formula`() {
    // Tier 1: Chhota Kaam (< 500) -> 5% GST
    val calc400 = ServiceProRepository.calculateWorkerRule(400)
    assertEquals(400, calc400.workerGetsPura)
    assertEquals(5, calc400.gstPercent)
    assertEquals(20, calc400.ownerGstAccount)
    assertEquals(420, calc400.customerTotal)
    assertEquals(210, calc400.advanceOnline50Percent)
    assertEquals(210, calc400.remaining50Percent)

    // Tier 2: Standard Kaam (500 to 1000) -> 8% GST
    val calc500 = ServiceProRepository.calculateWorkerRule(500)
    assertEquals(500, calc500.workerGetsPura)
    assertEquals(8, calc500.gstPercent)
    assertEquals(40, calc500.ownerGstAccount)
    assertEquals(540, calc500.customerTotal)
    assertEquals(270, calc500.advanceOnline50Percent)
    assertEquals(270, calc500.remaining50Percent)

    val calc1000 = ServiceProRepository.calculateWorkerRule(1000)
    assertEquals(1000, calc1000.workerGetsPura)
    assertEquals(8, calc1000.gstPercent)
    assertEquals(80, calc1000.ownerGstAccount)
    assertEquals(1080, calc1000.customerTotal)
    assertEquals(540, calc1000.advanceOnline50Percent)
    assertEquals(540, calc1000.remaining50Percent)

    // Tier 3: Bada Kaam (> 1000) -> 10% GST
    val calc1500 = ServiceProRepository.calculateWorkerRule(1500)
    assertEquals(1500, calc1500.workerGetsPura)
    assertEquals(10, calc1500.gstPercent)
    assertEquals(150, calc1500.ownerGstAccount)
    assertEquals(1650, calc1500.customerTotal)
    assertEquals(825, calc1500.advanceOnline50Percent)
    assertEquals(825, calc1500.remaining50Percent)
  }
}
