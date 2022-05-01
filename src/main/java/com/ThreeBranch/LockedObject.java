package com.ThreeBranch;

import java.util.concurrent.locks.*;

public class LockedObject<O> {
  private O o;
  private Lock l;
  
  public LockedObject(O o, Lock l) {
    this.o = o;
    this.l = l;
  }
  
  public Lock getLock() {
    return l;
  }
  
  public O get() {
    l.lock();
    try {
      return o;
    } finally {
      l.unlock();
    }
  }
}