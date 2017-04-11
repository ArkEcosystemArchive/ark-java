package io.ark.core

class Verification {
  List<String> errors = []

  public String toString(){
    errors.length > 0 ? errors.join(", ") : "Verified"
  }

}
