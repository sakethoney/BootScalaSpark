package com.example.demo.work

object PersonList {
  def find(name: String): Named = new JavaPerson(name)
}
