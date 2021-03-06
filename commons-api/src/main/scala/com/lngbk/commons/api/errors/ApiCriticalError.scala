package com.lngbk.commons.api.errors

/**
  * Created by beolnix on 10/09/16.
  */
class ApiCriticalError(val msg: String) extends Exception(msg)

class ApiVersionCheckFailure(val msg: String) extends Exception(msg)

class ApiInitializationFailure(val msg: String) extends Exception
