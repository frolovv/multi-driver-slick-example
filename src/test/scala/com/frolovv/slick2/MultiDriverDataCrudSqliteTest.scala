package com.frolovv.slick2

import org.specs2.mutable.Specification
import org.specs2.specification.Scope


/**
 * Created by valeryf on 5/12/15
 */
class MultiDriverDataCrudSqliteTest extends Specification {
  sequential

  "MultiDriverDataCrudSqliteTest with sqlite driver" should {
    "pass sanity check" in new ContextSqlite {
      crud.getById(data.id) should beNone
    }

    "insert data" in new ContextSqlite {
      crud.insert(data)

      crud.getById(data.id) should beSome(data)
    }

    "delete data" in new ContextSqlite {
      crud.insert(data)
      crud.delete(data)

      crud.getById(data.id) should beNone
    }

    "update data" in new ContextSqlite {
      crud.insert(data)
      crud.update(updatedData)

      crud.getById(data.id) should beSome(updatedData)
    }

    "throw an exception on primary key violation" in new ContextSqlite {
      crud.insert(data)
      crud.insert(data) should throwA[Exception]
    }
  }

  trait ContextSqlite extends Scope {
    val data = Data(1, "value")
    val updatedData = data.copy(value = "new value")
    val crud = MultiDriverDataCrud.sqlite()
  }

}
