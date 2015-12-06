package org.openurp.platform.api.cas.id

import java.util.concurrent.atomic.AtomicLong

/**
 * @author chaostone
 */
class DefaultLongNumericGenerator(count: AtomicLong) extends NumericGenerator {

  def this(initValue: Long) {
    this(new AtomicLong(initValue))
  }
  
  override def nextNumber: String = {
    val nextValue =
      if (this.count.compareAndSet(java.lang.Long.MAX_VALUE, 0)) java.lang.Long.MAX_VALUE
      else this.count.getAndIncrement()
    java.lang.Long.toString(nextValue)
  }

}