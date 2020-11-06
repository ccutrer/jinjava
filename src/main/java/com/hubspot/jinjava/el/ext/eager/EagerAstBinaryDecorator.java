package com.hubspot.jinjava.el.ext.eager;

import com.hubspot.jinjava.el.ext.DeferredParsingException;
import de.odysseus.el.tree.Bindings;
import de.odysseus.el.tree.impl.ast.AstBinary;
import de.odysseus.el.tree.impl.ast.AstNode;
import javax.el.ELContext;

public class EagerAstBinaryDecorator extends AstBinary implements EvalResultHolder {
  private Object evalResult;
  private EvalResultHolder left;
  private EvalResultHolder right;
  private Operator operator;

  public EagerAstBinaryDecorator(AstNode left, AstNode right, Operator operator) {
    this(
      EagerAstNodeDecorator.getAsEvalResultHolder(left),
      EagerAstNodeDecorator.getAsEvalResultHolder(right),
      operator
    );
  }

  private EagerAstBinaryDecorator(
    EvalResultHolder left,
    EvalResultHolder right,
    Operator operator
  ) {
    super((AstNode) left, (AstNode) right, operator);
    this.left = left;
    this.right = right;
    this.operator = operator;
  }

  @Override
  public Object eval(Bindings bindings, ELContext context) {
    if (evalResult != null) {
      return evalResult;
    }
    try {
      evalResult = super.eval(bindings, context);
    } catch (DeferredParsingException e) {
      StringBuilder sb = new StringBuilder();
      if (left.getEvalResult() != null) {
        sb.append(left.getEvalResult());
        sb.append(operator.toString());
        sb.append(e.getDeferredEvalResult());
      } else {
        sb.append(e.getDeferredEvalResult());
        sb.append(String.format(" %s ", operator.toString()));
        try {
          sb.append(((AstNode) right).eval(bindings, context));
        } catch (DeferredParsingException e1) {
          sb.append(e1.getDeferredEvalResult());
        }
      }
      throw new DeferredParsingException(sb.toString());
    }
    return evalResult;
  }

  @Override
  public Object getEvalResult() {
    return evalResult;
  }
}
