/*
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * General interface for nodes in the abstract syntax tree. Contains only the method toString which
 * is already inherited from Object, so the interface doesn't add any functionality. It only
 * provides a common super type for all elements in the AST.
 */
interface AST {
  public String toString(); // already inherited from Object
}
