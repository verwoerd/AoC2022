plugins {
  id("aoc.problem")
  kotlin("plugin.serialization") version "1.7.20"
}
project.application.mainClass.set("MainKt")

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}

