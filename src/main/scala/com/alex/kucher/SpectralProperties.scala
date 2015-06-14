package com.alex.kucher

object SpectralProperties {

  private val DB_CONSTANT: Double = 50

  /**
   *
   * @param width - width of window
   * @param height = height of window
   * @param m - half length of terms
   * @return - normalised to peak values converted to decibels
   */
  def dsp(width: Int, height: Int, m: Int, func: (Int) => (Double, Double, Double)): Array[Double] = {
    /*** Load one Half of Window (Excluding Central Value) into Array see ***/
    val see: Array[Double] = generateValues(m, func)
    val results: Array[Double] = Array.ofDim(width)

    /*** Estimate Spectrum ***/
    val w: Array[Double] = Array.fill(width)(1)
    for(n <- 0 until width) {
      val freq = math.Pi * n / width
      for(k <- 0 until m) {
        w(n) += 2 * see(k) * math.cos(k * freq)
      }
      /*** Normalise to Peak Value, Convert to Decibels ***/
      w(n) /= w(0)
      val multiplier = height / DB_CONSTANT
      val db: Double = math.max(20 * math.log(math.abs(w(n))) * 0.4343, -DB_CONSTANT)
      results(n) = db*multiplier
    }
    results
  }

  def generateValues(m: Int, func: (Int) => (Double, Double, Double)): Array[Double] = {
    val (a, b, c): (Double, Double, Double) = func(m)
    Array.range(0, m).map(k => a + b * math.cos((k + 1) * math.Pi / c))
  }

  def vonHann(m: Int): (Double, Double, Double) = (0.54, 0.46, m)

  def hamming(m: Int): (Double, Double, Double) = (0.5, 0.5, m+1)

  def getWinFunction(name: String): (Int) => (Double, Double, Double) = if(name.equalsIgnoreCase("hamming")) hamming else vonHann
}
