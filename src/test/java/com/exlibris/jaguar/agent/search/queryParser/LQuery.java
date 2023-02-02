package com.exlibris.jaguar.agent.search.queryParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.exlibris.jaguar.infra.utils.strings.StringUtils;


public class LQuery {

	public ExpBool mainExp = null;
	public ArrayList<ExpPrim> terms = new ArrayList<ExpPrim>();
	public boolean changedByTransfer=false;
	public LQuery(String query) {
		char[] qc = query.toCharArray();
		int expc = 0;
		ExpBool ex, ex1, ex2, ex3;

		Stack<ExpBool> expbq = new Stack<ExpBool>();
		StringBuffer sb = null;
		for (int i = 0; i < qc.length; i++) {
			char c = qc[i];
			switch (c) {
			case '(':
				sb = new StringBuffer();
				ExpBool e = new ExpExp();
				expbq.add(e);
				expc++;
				break;
			case ')':
				expc--;
				if (sb.length() > 0) {
					ExpPrim ep = new ExpPrim();
					ep.setTerm(sb.toString());
					expbq.add(ep);

					sb = new StringBuffer();
					terms.add(ep);
				}
				ex = expbq.pop();
				boolean ischanged = false;
				if (!expbq.isEmpty()) {
					ex1 = expbq.pop();

					if (ex1 instanceof ExpAnd) {
						((ExpAnd) ex1).setExp2(ex);

						if (!expbq.isEmpty()) {
							ex2 = expbq.pop();
							if(ex2 instanceof ExpField) {
								((ExpField) ex2).setTerm(ex1);
								
							}else if(ex2 instanceof ExpExp){
								((ExpExp) ex2).setExp(ex1);
								if(!expbq.isEmpty() && expbq.peek() instanceof ExpField) {
									ex3 = expbq.pop();
									((ExpField) ex3).setTerm(ex2);
									ex2 = ex3;
								}
							}
							ex1 = ex2;
						}
						ischanged = true;
					} else if (ex1 instanceof ExpOr) {
						((ExpOr) ex1).setExp2(ex);
						if (!expbq.isEmpty()) {
							ex2 = expbq.pop();
							if(ex2 instanceof ExpField) {
								((ExpField) ex2).setTerm(ex1);
								
							}else if(ex2 instanceof ExpExp){
								((ExpExp) ex2).setExp(ex1);
								if(!expbq.isEmpty() && expbq.peek() instanceof ExpField) {
									ex3 = expbq.pop();
									((ExpField) ex3).setTerm(ex2);
									ex2 = ex3;
								}
							}
							ex1 = ex2;
						}
						ischanged = true;
					} else if (ex1 instanceof ExpNot) {
						((ExpNot) ex1).setExp2(ex);
						if (!expbq.isEmpty()) {
							ex2 = expbq.pop();
							if(ex2 instanceof ExpField) {
								((ExpField) ex2).setTerm(ex1);
								
							}else if(ex2 instanceof ExpExp){
								((ExpExp) ex2).setExp(ex1);
								if(!expbq.isEmpty() && expbq.peek() instanceof ExpField) {
									ex3 = expbq.pop();
									((ExpField) ex3).setTerm(ex2);
									ex2 = ex3;
								}
							}
							ex1 = ex2;
						}
						ischanged = true;
					} else if (ex1 instanceof ExpField) {
						((ExpField) ex1).setTerm(ex);
						ischanged = true;
					} else if (ex1 instanceof ExpExp) {
						((ExpExp) ex1).setExp(ex);
						if (!expbq.isEmpty() && expbq.peek() instanceof ExpField) {
							ExpField exp2 = (ExpField) expbq.pop();
							exp2.setTerm(ex1);
							ex1 = exp2;
						}
						ischanged = true;
					}

					expbq.push(ex1);

				}
				if (!ischanged) {
					expbq.push(ex);
				}

				if (expc == 0) {
					mainExp = expbq.pop();
				}

				break;
			case '"':
				if (expbq.peek() instanceof ExpExac) {
					ExpExac eb = (ExpExac) expbq.pop();
					eb.setTerm(sb.toString());
					expbq.add(eb);

				} else {
					ExpExac ep = new ExpExac();
					expbq.add(ep);
					terms.add(ep);
				}
				sb = new StringBuffer();
				break;
			case ':':
				if (sb.length() > 0) {
					ExpField ef = new ExpField();
					ef.setField(sb.toString());
					expbq.add(ef);
				}
				break;
			case ' ':
				String t = sb.toString();
				switch (t.trim()) {
				case "AND":
					ExpAnd a = new ExpAnd();
					a.setExp1(expbq.pop());
					expbq.add(a);
					sb = new StringBuffer();
					break;
				case "OR":
					ExpOr o = new ExpOr();
					o.setExp1(expbq.pop());
					expbq.add(o);
					sb = new StringBuffer();
					break;
				case "NOT":
					ExpNot n = new ExpNot();
					n.setExp1(expbq.pop());
					expbq.add(n);
					sb = new StringBuffer();
					break;
				default:

					ExpBool exbs = expbq.pop();
					ischanged = false;
					if (exbs instanceof ExpExp) {
						sb.append(c);
					}
					if (exbs instanceof ExpExac && exbs.isNotFin()) {
						sb.append(c);
					}
					if (!expbq.isEmpty() && !exbs.isNotFin()) {
						ex1 = expbq.pop();
						if (ex1 instanceof ExpAnd) {
							if (ex1.isNotFin()) {
								((ExpAnd) ex1).setExp2(exbs);
								ischanged = true;
							}
						} else if (ex1 instanceof ExpOr) {
							if (ex1.isNotFin()) {
								((ExpOr) ex1).setExp2(exbs);
								ischanged = true;
							}
						} else if (ex1 instanceof ExpNot) {
							if (ex1.isNotFin()) {
								((ExpNot) ex1).setExp2(exbs);
								ischanged = true;
							}
						}

						expbq.push(ex1);

					}

					if (!ischanged) {
						expbq.push(exbs);
					}

				}
				break;
			default:
				if(sb==null) {
					ExpPrim ep = new ExpPrim();
					ep.setTerm(query);
					mainExp=ep;
					terms.add(ep);
					break;
					
				}else {
					sb.append(c);
				}
			}

		}

	}

	@Override
	public String toString() {
		return mainExp.toString();
	}

	public void print() {
		System.out.println("Main Exp:" + this);

	}

	public void printTerms() {
		for (ExpBool eb : this.terms) {
			System.out.println(eb);
		}
	}

	public ArrayList<ExpPrim> getTerms() {
		return terms;
	}

	
	public List<ExpPrim> getTermsOfPossitive() {
		return terms.stream().filter(e->e.isPossitive()).collect(Collectors.toList());
	}

	public void trunkStringOnTerms(String rem) {
		for (ExpPrim eb : this.terms) {
			String term=eb.getTerm();
			term=StringUtils.trunkByString(term, rem);
			eb.setTerm(term);
		}		
	}
	
	/**
	 * Run with all prim terms in query and try to change the values by transfer
	 * @param merger
	 * @param lang
	 */
	public void changeExpAndAddAsOr(BiFunction<String, String, String> transfer, String lang) {
		ArrayList<ExpPrim> newterms = new ArrayList<LQuery.ExpPrim>();
		for (ExpPrim ex : this.getTerms()) {
			String newTerm=transfer.apply(ex.getTerm(), lang);
			
			if(!org.apache.commons.lang.StringUtils.isEmpty(newTerm) && !newTerm.equals(ex.getTerm())) {
				ExpPrim n = this.getNewPrim();				
				n.setTerm(newTerm);
				newterms.add(n);
				changedByTransfer=true;
				ex.addOrValue(n);
			}
		}	
		this.terms.addAll(newterms);
	}
	public boolean isChangedByTransfer() {
		return changedByTransfer;
	}
	
	public boolean isNotExpressionQuery() {
		if(this.mainExp==null) return true;
		if(!(this.mainExp instanceof ExpExp)) return true;
		return false;
		
	}
	
	public interface ExpBool {
		public boolean isNotFin();

		public void setParent(ExpBool par);

		public void addOrValue(ExpBool t);

		public void updateThisArgument(ExpBool t, ExpBool o);
		
		public boolean isPossitive();
	}

	private class ExpExp implements ExpBool {
		private ExpBool exp = null;

		public ExpExp() {
		}

		public void setExp(ExpBool exp1) {
			exp1.setParent(this);
			this.exp = exp1;
		}

		@Override
		public String toString() {
			return "(" + exp + ")";
		}

		@Override
		public boolean isNotFin() {

			return this.exp == null;
		}

		private ExpBool par = null;

		@Override
		public void setParent(ExpBool par) {
			this.par = par;

		}

		@Override
		public void addOrValue(ExpBool t) {
			ExpOr ex = new ExpOr();
			ex.setExp1(this);
			ex.setExp2(t);
			par.updateThisArgument(this, ex);
		}

		@Override
		public void updateThisArgument(ExpBool t, ExpBool o) {
			if (this.exp == t) {
				o.setParent(this);
				this.exp = o;
			}

		}

		@Override
		public boolean isPossitive() {
			if(this.par==null) return true;
			if(this.par.isPossitive()) return true;
			if(this.par instanceof ExpNot) {
				if(((ExpNot)this.par).getExp2().equals(this)) return false;
				else return true;
			}else {
				return false;
			}
		}

	}

	private class ExpAnd implements ExpBool {
		private ExpBool exp1 = null;
		private ExpBool exp2 = null;

		public ExpAnd() {

		}

		public ExpBool getExp1() {
			return exp1;
		}

		public ExpBool getExp2() {
			return exp2;
		}

		public void setExp1(ExpBool exp1) {
			exp1.setParent(this);
			this.exp1 = exp1;

		}

		public void setExp2(ExpBool exp2) {
			exp2.setParent(this);
			this.exp2 = exp2;
		}

		@Override
		public String toString() {
			return exp1 + " AND " + exp2;
		}

		@Override
		public boolean isNotFin() {
			return exp2 == null;
		}

		private ExpBool par = null;

		@Override
		public void setParent(ExpBool par) {
			this.par = par;

		}

		@Override
		public void addOrValue(ExpBool t) {
			ExpOr ex = new ExpOr();
			ex.setExp1(this);
			ex.setExp2(t);
			par.updateThisArgument(this, ex);
		}

		@Override
		public void updateThisArgument(ExpBool t, ExpBool o) {
			if (this.exp1 == t) {
				o.setParent(this);
				this.exp1 = o;
			} else {
				if (this.exp2 == t) {
					o.setParent(this);
					this.exp2 = o;
				}
			}

		}

		@Override
		public boolean isPossitive() {
			if(this.par==null) return true;
			if(this.par.isPossitive()) return true;
			if(this.par instanceof ExpNot) {
				if(((ExpNot)this.par).getExp2().equals(this)) return false;
				else return true;
			}else {
				return false;
			}
		}

	}

	private class ExpOr implements ExpBool {
		private ExpBool exp1 = null;
		private ExpBool exp2 = null;

		public ExpOr() {

		}

		public ExpBool getExp1() {
			return exp1;
		}

		public ExpBool getExp2() {
			return exp2;
		}

		public void setExp1(ExpBool exp1) {
			exp1.setParent(this);
			this.exp1 = exp1;

		}

		public void setExp2(ExpBool exp2) {
			exp2.setParent(this);
			this.exp2 = exp2;
		}

		@Override
		public String toString() {
			return exp1 + " OR " + exp2;
		}

		@Override
		public boolean isNotFin() {
			return exp2 == null;
		}

		private ExpBool par = null;

		@Override
		public void setParent(ExpBool par) {
			this.par = par;

		}

		@Override
		public void addOrValue(ExpBool t) {
			ExpOr ex = new ExpOr();
			ex.setExp1(this);
			ex.setExp2(t);
			par.updateThisArgument(this, ex);
		}

		@Override
		public void updateThisArgument(ExpBool t, ExpBool o) {
			o.setParent(this);
			if (this.exp1 == t) {
				o.setParent(this);
				this.exp1 = o;
			} else {
				if (this.exp2 == t) {
					o.setParent(this);
					this.exp2 = o;
				}
			}

		}

		@Override
		public boolean isPossitive() {
			if(this.par==null) return true;
			if(this.par.isPossitive()) return true;
			if(this.par instanceof ExpNot) {
				if(((ExpNot)this.par).getExp2().equals(this)) return false;
				else return true;
			}else {
				return false;
			}
		}
	}

	private class ExpNot implements ExpBool {
		private ExpBool exp1 = null;
		private ExpBool exp2 = null;

		public ExpNot() {

		}

		public ExpBool getExp1() {
			return exp1;
		}

		public ExpBool getExp2() {
			return exp2;
		}

		public void setExp1(ExpBool exp1) {
			exp1.setParent(this);
			this.exp1 = exp1;

		}

		public void setExp2(ExpBool exp2) {
			exp2.setParent(this);
			this.exp2 = exp2;
		}

		@Override
		public String toString() {
			return exp1 + " NOT " + exp2;
		}

		@Override
		public boolean isNotFin() {
			return exp2 == null;
		}

		private ExpBool par = null;

		@Override
		public void setParent(ExpBool par) {
			this.par = par;

		}

		@Override
		public void addOrValue(ExpBool t) {
			ExpOr ex = new ExpOr();
			ex.setExp1(this);
			ex.setExp2(t);
			par.updateThisArgument(this, ex);
		}

		@Override
		public void updateThisArgument(ExpBool t, ExpBool o) {
			if (this.exp1 == t) {
				this.exp1 = o;
				o.setParent(this);
			} else {
				if (this.exp2 == t) {
					this.exp2 = o;
					o.setParent(this);
				}
			}

		}

		@Override
		public boolean isPossitive() {
			return false;
		}

	}

	public ExpPrim getNewPrim() {
		return new ExpPrim();
	}

	public class ExpPrim implements ExpBool {
		private String term;

		public ExpPrim() {
		}

		public void setTerm(String term) {
			this.term = term;
		}
		public String getTerm() {
			return term;
		}
		@Override
		public String toString() {
			return term;
		}

		@Override
		public boolean isNotFin() {
			return term == null;
		}

		private ExpBool par = null;

		@Override
		public void setParent(ExpBool par) {
			this.par = par;

		}

		@Override
		public void addOrValue(ExpBool t) {
			ExpOr ex = new ExpOr();
			ex.setParent(this.par);
			ExpExp ee = new ExpExp();
			ExpExp ee1 = new ExpExp();
			ee.setExp(this);
			ee1.setExp(t);
			ex.setExp1(ee);
			ex.setExp2(ee1);
			if(ex.par!=null) {
				ex.par.updateThisArgument(this, ex);
			}else {
				mainExp=ex;
			}
		}

		@Override
		public void updateThisArgument(ExpBool t, ExpBool o) {

		}

		@Override
		public boolean isPossitive() {
			if(this.par==null) return true;
			if(this.par.isPossitive()) return true;
			if(this.par instanceof ExpNot) {
				if(((ExpNot)this.par).getExp2().equals(this)) return false;
				else return true;
			}else {
				return false;
			}
		}

	}

	private class ExpExac extends ExpPrim implements ExpBool {

		@Override
		public String toString() {
			return "\"" + super.getTerm() + "\"";
		}


	}

	private class ExpField implements ExpBool {
		private String field;
		private ExpBool term;

		public ExpField() {
		}

		public void setField(String field) {
			this.field = field;
		}

		public void setTerm(ExpBool term) {
			this.term = term;
		}

		@Override
		public String toString() {
			return this.field + ":" + this.term;
		}

		@Override
		public boolean isNotFin() {
			return term == null;
		}

		private ExpBool par = null;

		@Override
		public void setParent(ExpBool par) {
			this.par = par;

		}

		@Override
		public void addOrValue(ExpBool t) {
			ExpOr ex = new ExpOr();
			ex.setExp1(this);
			ex.setExp2(t);
			this.par.updateThisArgument(this, ex);
		}

		@Override
		public void updateThisArgument(ExpBool t, ExpBool o) {

		}

		@Override
		public boolean isPossitive() {
			if(this.par==null) return true;
			if(this.par.isPossitive()) return true;
			if(this.par instanceof ExpNot) {
				if(((ExpNot)this.par).getExp2().equals(this)) return false;
				else return true;
			}else {
				return false;
			}
		}

	}
	
	
	
	public static void main(String[] args) {
		String exp = "(((war) AND title:(\"peace\")  NOT (\"Black mamaba\" )) OR ( swstitle:(\"green*\") NOT sub:(sharm) AND usertag:(barmaglot vs alice ) AND (fuck vs tedy bear)) OR (number))";
		System.out.println("Orig Exp:"+exp);
		LQuery lq = new LQuery(exp);
		lq.print();
		lq.printTerms();
		lq.changeExpAndAddAsOr((s,l)->(s.toUpperCase()), "ENG");
		lq.print();

		exp = "(((Green peace ) NOT (\"Black mamaba\" )) OR (sub:(Water flaw cintetic ) AND title:(Fingipul )))";
		System.out.println("Orig Exp:"+exp);
		lq = new LQuery(exp);
		lq.print();
		lq.printTerms();
		lq.changeExpAndAddAsOr((s,l)->(s.toUpperCase()), "ENG");
		lq.print();

		exp = "(war)";
		System.out.println("Orig Exp:"+exp);
		lq = new LQuery(exp);
		lq.print();
		lq.printTerms();
		lq.changeExpAndAddAsOr((s,l)->(s.toUpperCase()), "ENG");
		lq.print();

		exp = "(Green peace vs mamba)";
		System.out.println("Orig Exp:"+exp);
		lq = new LQuery(exp);
		lq.print();
		lq.printTerms();
		lq.changeExpAndAddAsOr((s,l)->(s.toUpperCase()), "ENG");
		lq.print();

		exp = "(((日 報 ) NOT (民 )) OR (sub:(人 ) AND title:(報 )))";
		System.out.println("Orig Exp:"+exp);
		lq = new LQuery(exp);
		lq.print();
		lq.printTerms();
		lq.changeExpAndAddAsOr((s,l)->(s.toUpperCase()), "ENG");
		lq.print();
	}

	
}
