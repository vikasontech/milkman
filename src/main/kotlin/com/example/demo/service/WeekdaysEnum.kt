package com.example.demo.service


enum class WeekDays(value: Int) {
  Sunday(0),
  Monday(1),
  Tuesday(2),
  Wednesday(3),
  Thursday(4),
  Friday(5),
  Saturday(6),
  NONE(-1);

  companion object {
    fun new(value: Int): WeekDays {
      var res: WeekDays = NONE
      when (value) {
        0 -> res = Sunday
        1 -> res = Monday
        2 -> res = Tuesday
        3 -> res = Wednesday
        4 -> res =
            Thursday
        5 -> res = Friday
        6 -> res = Saturday
      }
      return res
    }
  }
}