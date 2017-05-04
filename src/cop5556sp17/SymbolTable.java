package cop5556sp17;

import java.util.HashMap;
import java.util.Stack;

import cop5556sp17.AST.Dec;

public class SymbolTable {

	public Stack<Integer> scope = new Stack<Integer>();
	public HashMap<String, Dec> valueAttribute = new HashMap<>();
	public int currentScope = 0;
	public int nextScope = 1;

	// TODO add fields

	/**
	 * to be called when block entered
	 */
	public void enterScope() {
		// TODO: IMPLEMENT THIS
		currentScope = nextScope;
		scope.push(currentScope);
		nextScope += 1;
	}

	/**
	 * leaves scope
	 */
	public void leaveScope() {
		// TODO: IMPLEMENT THIS
		scope.pop();
		if (scope.size() != 0) {
			currentScope = scope.peek();
			nextScope = currentScope;
		}
	}

	public boolean insert(String ident, Dec dec) {
		// TODO: IMPLEMENT THIS
		String keyValue = ident + "@" + currentScope;
		if (valueAttribute.containsKey(keyValue)) {
			return false;
		} else {
			valueAttribute.put(keyValue, dec);
			return true;
		}
	}

	public Dec lookup(String ident) {
		// TODO: IMPLEMENT THIS
		Dec findValue = null;
		int scope = currentScope;
		String key = "";
		while (scope >= 0) {
			key = ident + "@" + scope;
			findValue = valueAttribute.get(key);
			if (findValue == null) {
				scope = scope - 1;
			} else {
				return findValue;
			}
		}
		return null;
	}

	public SymbolTable() {
		// TODO: IMPLEMENT THIS
		scope.push(currentScope);
	}

	@Override
	public String toString() {
		// TODO: IMPLEMENT THIS
		return "";
	}

}
