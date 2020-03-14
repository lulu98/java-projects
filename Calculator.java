package test_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Calculator {
	
	public void calculateExpression(String exp) {
		//String exp = "((2*3)+(5*4))-9";
		List<String> infix = prepareString(exp);
		//printList(infix);
        List<String> postfix = infixToPostfix(infix);
        //printList(postfix);
        double result = evaluatePostfixExpression(postfix);
        System.out.println("Result: " + result);
	}
	
	private void printList(List<String> myList) {
		for(int i = 0; i < myList.size(); i++) {
			System.out.print(myList.get(i));
		}
		System.out.println();
	}
	
	private List<String> prepareString(String exp){
        List<String> infixNotation = new ArrayList<>();
        String temp = "";
        for(int i = 0; i < exp.length(); i++){
            if(	exp.charAt(i)=='+' || exp.charAt(i)=='-' ||
            	exp.charAt(i)=='*' || exp.charAt(i)=='/' ||
                exp.charAt(i)=='(' || exp.charAt(i)==')' || 
                exp.charAt(i)=='^'){
                if(temp.length() > 0) {
                	infixNotation.add(temp);
                    temp = "";
                }
            	infixNotation.add(String.valueOf(exp.charAt(i)));
            }else {
            	temp += exp.charAt(i);
            }
        }
        if(temp.length()>0) {
        	infixNotation.add(temp);
        }
        return infixNotation;
    }

    private List<String> infixToPostfix(List<String> exp){
        Stack<String> stack = new Stack<String>();
        List<String> result = new ArrayList<>();
        
        for(int i = 0; i < exp.size(); i++) {
        	String c = exp.get(i);
        	if(isOperand(c)) {
        		result.add(c);
        	}else if(c.equals("(")) {
        		stack.push(c);
        	}else if(c.equals(")")) {
        		while(	stack.size() != 0 &&
        				!stack.peek().equals("(")) {
        			result.add(stack.pop());
        		}
        		if(	stack.size() != 0 && !stack.peek().equals("(")) {
        			System.out.println("Invalid Expression!");
        		}else {
        			stack.pop();
        		}
        	}else {
        		while(	stack.size() != 0 && getPrecedence(c) <= getPrecedence(stack.peek())) {
        			result.add(stack.pop());
        		}
        		stack.push(c);
        	}
        }
        while(stack.size() != 0) {
        	result.add(stack.pop());
        }
        
        return result;
    }

    private boolean isOperand(String op) {
    	return !(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("(") || op.equals(")") || op.equals("^"));
    }
    
    private boolean isOperator(String op) {
    	return (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("^"));
    }
    
    private int getPrecedence(String op) {
    	switch(op.charAt(0)) {
    	case '+':
    	case '-':
    		return 1;
    	case '*':
    	case '/':
    		return 2;
    	case '^':
    		return 3;
    	}
    	return -1;
    }
    
    private double evaluatePostfixExpression(List<String> exp){
        Stack<Double> stack = new Stack<Double>();
        for(int i = 0 ; i < exp.size(); i++) {
        	String c = exp.get(i);
        	if(isOperand(c)) {
        		stack.push(Double.parseDouble(c));
        	}else if(isOperator(c)) {
        		double op2 = stack.pop();
        		double op1 = stack.pop();
        		double result = perform(exp.get(i),op1,op2);
        		stack.push(result);
        	}
        }
        return stack.pop();
    } 
    
    private double perform(String operation, double op1, double op2) {
    	switch(operation) {
    	case "+":
    		return op1 + op2;
    	case "-":
    		return op1 - op2;
    	case "*":
    		return op1 * op2;
    	case "/":
    		return op1 / op2;
    	case "^":
    		return Math.pow(op1, op2);
    	}
    	return 0;
    }
}
