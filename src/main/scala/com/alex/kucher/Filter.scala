package com.alex.kucher

object Filter {

  /**
   * @param in - vector of input data (vector after fft transform)
   * @param filterLength = length of filter (biggest length - slower program)
   * @param frameRate - Sampling frequency input
   * @param frequencyBandwidth - ( = 20) The frequency bandwidth
   * @param frequencyBandAttenuation - ( = 50) Frequency band attenuation
   * @param func - window function
   * @return
   */
  def filter(in: Array[Double], filterLength: Int, frameRate: Int, frequencyBandwidth: Int, frequencyBandAttenuation: Int, func: (Int, Int) => Double): Array[Double] = {
    val out: Array[Double] = Array.ofDim(in.length)
    val h: Array[Double] = Array.ofDim(filterLength) //impulse characteristic of filter
    val hId: Array[Double] = Array.ofDim(filterLength) //ideal impulse characteristic
    val w: Array[Double] = Array.ofDim(filterLength) //weight function

    //Calculation of the impulse response of filter
    val fc: Double = (frequencyBandwidth + frequencyBandAttenuation) / (2.0 * frameRate)

    for (i <- 0 until filterLength) {
      if (i == 0){
        hId(i) = 2*math.Pi*fc
      } else {
        hId(i) = math.sin(2*math.Pi*fc*i)/(math.Pi*i)
      }
      w(i) = func(i, filterLength)
      h(i) = hId(i) * w(i)
    }

    //Normalization of the impulse characteristic
    val sum = h.sum
    val newH = h.map(_ / sum)

    //filtering input data
    for (i <- in.indices){
      out(i) = 0.0
      for (j <- 0 until filterLength-1) {
        if(i-j >= 0){
          out(i) += newH(j)*in(i-j)
        }
      }
    }
    out
  }

  /**
   * weight function for square window
   * max side lobe level -13 db
   * @param i - index of current element of input array
   * @param n - length of input array
   */
  def square(i: Int, n: Int): Double = if(i >= 0 && i < n) 1 else 0

  /**
   * weight function for window of Hann
   * side lobe level -31.5 db
   * @param i - index of current element of input array
   * @param n - length of input array
   */
  def hann(i: Int, n: Int): Double = 0.5 * (1 - math.cos((2 * math.Pi * i) / (n - 1)))

  /**
   * weight function for window of Hemming
   * side lobe level -42 db
   * @param i - index of current element of input array
   * @param n - length of input array
   */
  def hamming(i: Int, n: Int): Double = 0.53836 - 0.46164 * math.cos((2 * math.Pi * i) / (n - 1))

  /**
   * weight function for window of Blackman
   * side lobe level -58 db (alpha=0.16)
   * @param i - index of current element of input array
   * @param n - length of input array
   */
  def blackman(i: Int, n: Int): Double = {
    val alpha: Double = 0.16
    val a0: Double = (1 - alpha) / 2
    val a1: Double = 0.5
    val a2: Double = alpha / 2
    a0 - a1 * math.cos((2 * math.Pi * i) / (n - 1)) + a2 * math.cos((4 * math.Pi * i) / (n - 1))
  }

  def getWinFunction(name: String): (Int, Int) => Double =
    if(name.equalsIgnoreCase("blackman")) blackman
    else if (name.equalsIgnoreCase("hamming")) hamming
    else if (name.equalsIgnoreCase("hann")) hann
    else square
}
