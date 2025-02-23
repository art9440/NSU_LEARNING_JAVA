package org.calculator.app;

import org.calculator.exeptions.NoSuchVariableInMapException;
import org.calculator.exeptions.StackIsEmptyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Context {
    private Stack<Double> calcStack = new Stack<>();
    private Map<String, Double> varValues = new HashMap<>();

    public Map<String, Double> getMap(){
        return varValues;
    }

    public void setMap(Map<String, Double> varValues){
        this.varValues = varValues;
    }

    public void putToMap(String variable, double value){
        if (varValues.containsKey(variable)) {
            System.out.println("🔄 Обновление переменной: " + variable + " = " + value);
        } else {
            System.out.println("✅ Создание переменной: " + variable + " = " + value);
        }
        varValues.put(variable, value);

        //for (Map.Entry<String, Double> entry : varValues.entrySet()) {
           //System.out.println(entry.getKey() + " = " + entry.getValue());
        //}
    }

    public double getFromMap(String variable) throws NoSuchVariableInMapException {
        if (!varValues.containsKey(variable)) {
            throw new NoSuchVariableInMapException("Can`t find definition of variable: " + variable);
        }
        return varValues.get(variable);
    }

    public void push(double value){
        calcStack.push(value);
        System.out.println(value + " is on top of the stack");
    }

    public double pop() throws StackIsEmptyException {
        if (calcStack.empty()){
            throw new StackIsEmptyException("No values in calculator.");
        }
        else {
            return calcStack.pop();
        }
    }


}
