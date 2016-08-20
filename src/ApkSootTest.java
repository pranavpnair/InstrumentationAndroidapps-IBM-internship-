import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Modifier;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Transform;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.JastAddJ.AssignExpr;
import soot.JastAddJ.IfStmt;
import soot.dava.toolkits.base.renamer.infoGatheringAnalysis;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.JasminClass;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.internal.JIdentityStmt;
import soot.options.Options;
import soot.util.Chain;
import soot.util.JasminOutputStream;


public class ApkSootTest {
	static int id =0 ;
	static Type first, second, third;
    static String classextendingasync;
	public static void main(String[] args) throws IOException {
		String dir_class_files = "C:/Users/pranav_2/workspace/ThreadingAsyncTask.apk";
		TrySoot.setSootClassPath(dir_class_files);
		//-android-jars C:\android-platforms-master -process-dir C:\Users\\Downloads\AndroidCalculator.apk
		 List<String> argsList = new ArrayList<String>();
		   argsList.addAll(Arrays.asList(new String[]{	
				   "-pp",
		     "-android-jars",
		   "C:/android-platforms-master",
		    "-no-bodies-for-excluded",
		     "-w",// for call graph
		     "-process-dir",
		     dir_class_files,
		     "-p", "jb", "use-original-names",
		     "-p", "cg", "all-reachable:true", // for call graph
		     "-p", "cg", "implicit-entry:true", // for call graph	     
		     "-f","J"
		   }));
		 args = argsList.toArray(new String[0]);
		 

	
		
		 //Scene.v().loadClassAndSupport("java.lang.Object");

//		 Scene.v().loadClassAndSupport("java.lang.System");
		 
		
		
		//prefer Android APK files// -src-prec apk
		Options.v().set_src_prec(Options.src_prec_apk);
		
		//output as APK, too//-f J
//		Options.v().set_output_format(Options.output_format_dex);
		
        // resolve the PrintStream and System soot-classes
		Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
        
        
        
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new BodyTransformer() {
        	
    	@Override
    	protected void internalTransform(Body b, String arg1,
    			Map<String, String> arg2) {
    		final PatchingChain<Unit> units = b.getUnits();
    		SootClass c = b.getMethod().getDeclaringClass();
    		//System.out.println(c.getJavaStyleName()+ " -> "+b.getMethod().getSignature());
    		if(c.hasSuperclass()){
    			SootClass scl = c.getSuperclass();
    			
    			if(scl.getName() == "android.os.AsyncTask"){
    					classextendingasync=c.getName();
    					//System.out.println("Param types " + b.getMethod().getParameterTypes());
    				
    					SootMethod m= b.getMethod();
    					//System.out.println(m.getName());
    					if(m.getName().equals("doInBackground")) 
    					{
    							
				//			System.out.println("ParamNames " + b.getParameterLocals());
//		
    						List<Type> doinbgd = m.getParameterTypes();
    					//	System.out.println(doinbgd);
    						if(!doinbgd.get(0).toString().equals("java.lang.Object[]")){
    							if(doinbgd.get(0) instanceof ArrayType){
    								first = ((ArrayType)doinbgd.get(0)).getArrayElementType();
    							}
    						}
    					}
    					else if(m.getName().equals("onProgressUpdate"))
    					{
    						List<Type> doinbgd = m.getParameterTypes();
    						if(!doinbgd.get(0).toString().equals("java.lang.Object[]")){
    							if(doinbgd.get(0) instanceof ArrayType){
    								second = ((ArrayType)doinbgd.get(0)).getArrayElementType();
    							}
    						}
    					}
    					else if(m.getName().equals("onPostExecute"))
    					{
    						if(!m.getParameterTypes().get(0).toString().equals("java.lang.Object")){    							
        						third =m.getParameterTypes().get(0);
    						}
    					}        					
    				}
    			
    			
    				
    			}
    		}
        }));
        
        
		soot.Main.main(args);
        
	//	System.out.println(first + " " + second + " " + third);
      
		Options.v().set_src_prec(Options.src_prec_apk);
		
		//output as APK, too//-f J
//		Options.v().set_output_format(Options.output_format_dex);
		
        // resolve the PrintStream and System soot-classes
		Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);

        Scene.v().loadClassAndSupport("java.lang.Object");
        Scene.v().loadClassAndSupport("java.lang.System");
        Scene.v().loadClassAndSupport("android.util.Log");
        
        Scene.v().addBasicClass("android.util.Log",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
        
		 SootClass sClass = new SootClass("Info", Modifier.PUBLIC);

		 sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
		 Scene.v().addClass(sClass);
		 SootField m1= new SootField("id",RefType.v("java.lang.Integer"));

		 SootField m2= new SootField("doinbgd",first);
		 SootField m3= new SootField("onprgspudate",second);
		 SootField m4= new SootField("onpostexec",third);
		 sClass.addField(m1);
		 sClass.addField(m2);
		 sClass.addField(m3);
		 sClass.addField(m4);
		
	
		 String fileName = SourceLocator.v().getFileNameFor(sClass, Options.output_format_class);
	        OutputStream streamOut = new JasminOutputStream(
	                                    new FileOutputStream(fileName));
	        PrintWriter writerOut = new PrintWriter(
	                                    new OutputStreamWriter(streamOut));
	        JasminClass jasminClass = new soot.jimple.JasminClass(sClass);
	        jasminClass.print(writerOut);
	        writerOut.flush();
	        streamOut.close();

	        soot.G.reset();
	        
	        List<String> argsList1 = new ArrayList<String>();
			   argsList1.addAll(Arrays.asList(new String[]{	
					   "-pp",
			    "-no-bodies-for-excluded",
			     "-w",// for call graph
			     "-process-dir",
			     dir_class_files,
			     "-p", "jb", "use-original-names",
			     "-p", "cg", "all-reachable:true", // for call graph
			     "-p", "cg", "implicit-entry:true", // for call graph	     
			     "-f","J"
			   }));
			 args = argsList1.toArray(new String[0]);
	    	TrySoot.setSootClassPath(dir_class_files);			 
			
	    	Options.v().set_src_prec(Options.src_prec_apk);
			Options.v().set_android_jars("C:/android-platforms-master");
	        // resolve the PrintStream and System soot-classes
			Options.v().no_bodies_for_excluded();
			
//			Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
//	        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);

			Scene.v().addBasicClass("android.util.Log",SootClass.SIGNATURES);	        
	        Scene.v().loadClassAndSupport("android.util.Log");	        
	        
	        
	        
	        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter1", new BodyTransformer() {

	        	@Override
	        	protected void internalTransform(Body b, String arg1,
	        			Map<String, String> arg2) {
	        		final PatchingChain<Unit> units = b.getUnits();
	        		
	        		
	        		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
						final Unit u = iter.next();
						u.apply(new AbstractStmtSwitch() {			
							public void caseInvokeStmt(InvokeStmt stmt) {
								InvokeExpr invokeExpr = stmt.getInvokeExpr();
								if(invokeExpr.getMethod().getName().equals("execute")) 
								{
									
									if(invokeExpr.getMethod().getDeclaringClass().getName().equals("android.os.AsyncTask"))
									{
									
										Local info = addInfo(b,sClass);	
										// info i1 = new Info();
										units.insertBefore(Jimple.v().newAssignStmt( 
							                      info, Jimple.v().newNewExpr(sClass.getType())), u);
									
										// i1.id = constantsvalue in this.id;
										Value var1 = Jimple.v().newInstanceFieldRef(info, sClass.getFieldByName("id").makeRef());
										AssignStmt assignmentStmt1 = Jimple.v().newAssignStmt(var1, IntConstant.v(id));
										
										units.insertBefore(assignmentStmt1,u );	
										Value var2 = Jimple.v().newInstanceFieldRef(info, sClass.getFieldByName("doinbgd").makeRef());
										AssignStmt assignmentStmt2 = Jimple.v().newAssignStmt(var2, invokeExpr.getArg(0));
										units.insertBefore(assignmentStmt2, u);
										b.validate();
									}
								}
							}	
						});
	        		}
	        		
	        		SootClass c = b.getMethod().getDeclaringClass();
	        		if(c.hasSuperclass()){
	        			SootClass scl = c.getSuperclass();
	        			
	        			if(scl.getName() == "android.os.AsyncTask"){
	        					
	        				List<Type> params = new ArrayList<Type>();
	        				params.add(sClass.getType().makeArrayType());
	        				SootMethod m= b.getMethod();
	        					
	        				if(m.getName().equals("doInBackground"))
	        				{
	        					m.setReturnType(sClass.getType());
	        					List<Type> doinbgd = m.getParameterTypes();
	            				//	System.out.println(doinbgd);
	            				if(!doinbgd.get(0).toString().equals("java.lang.Object[]"))
            					{
            						int len=b.getParameterLocals().size();
            						m.setParameterTypes(params);
            						//set param name
            						changelocal(b,sClass,units,first,len);      						
            						
            						addlogstmt(b,units);				//insert at end also
            					}
	        				}
        					else if(m.getName().equals("onProgressUpdate"))
        					{
        						List<Type> doinbgd1 = m.getParameterTypes();
            					//	System.out.println(doinbgd);
            					if(!doinbgd1.get(0).toString().equals("java.lang.Object[]"))
            						m.setParameterTypes(params);
        					}
        					else if(m.getName().equals("onPostExecute"))
        					{
        						List<Type> doinbgd1 = m.getParameterTypes();
            			//		System.out.println("TYPES in onpost:" + doinbgd1);
            					if(!doinbgd1.get(0).toString().equals("java.lang.Object"))
            						m.setParameterTypes(params);
        					}        					
	        			}   		
	            	}
	        	}

				
	       })); 
		//System.out.println(first + " " + second + " " + third);
	    soot.Main.main(args);
			
	}

	
	private static void changelocal(Body b,SootClass sClass,PatchingChain<Unit> units, Type first,int len) {
		// TODO Auto-generated method stub
		Local arg = Jimple.v().newLocal("r", ArrayType.v(sClass.getType(),1));  
		b.getLocals().add(arg);
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
						Stmt s = (Stmt)u;
			if(s instanceof JIdentityStmt){		
				JIdentityStmt jid = (JIdentityStmt)s;
				ValueBox v1=jid.leftBox;
				if(v1.getValue().toString().equals("$r1"))
				{
					ValueBox v2 = jid.rightBox;
					Unit toInsert = Jimple.v().newAssignStmt(v1.getValue(), Jimple.v().newNewArrayExpr(RefType.v("java.lang.Integer"),IntConstant.v(len) ));
					v1.setValue(arg);
					units.insertAfter(toInsert, u);
					
				}
				//add loop here
			}
		}
	}
	
	
	static void addlogstmt(Body b,PatchingChain<Unit> units)
	 {
		 for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
				final Unit u = iter.next();
	         //   System.out.println(u);
	            
			   
			        	  Local tmpRef = addTmpRef(b);
			         
			        	  Local tmpString1 = addTmpString1(b);
			        	  Local tmpString2 = addTmpString2(b);
			                     
			           // inserting "tmpRef = android.util.Log.i;" 
			               units.insertAfter(Jimple.v().newAssignStmt( 
			                             tmpRef, Jimple.v().newStaticFieldRef( 
			                             Scene.v().getField("<android.util.Log: int v>").makeRef())), u);

			 

			               String s1= "TAG"; //declare tag
			               units.insertAfter(Jimple.v().newAssignStmt(tmpString1,
			                             StringConstant.v(s1)), u);
			               
			               String s2 =" \"ID:\"+res[0].id+\" doinbackground start time : \" ";
			               units.insertAfter(Jimple.v().newAssignStmt(tmpString2,
			                            StringConstant.v(s2)), u);
			               
			               // insert "tmpRef.i(s1,s2);" 
			               SootMethod toCall = Scene.v().getSootClass("android.util.Log").getMethod("int v(java.lang.String,java.lang.String");                    
			               units.insertAfter(Jimple.v().newInvokeStmt(
			                             Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(),tmpString1, tmpString2 )), u);
			               
			               //check that we did not mess up the Jimple
			               b.validate();

			}

	 }
	
	   static Local addTmpRef(Body body)
	   {
	   Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("android.util.Log"));
	   body.getLocals().add(tmpRef);
	   return tmpRef;
	   }
	      
	   static Local addTmpString1(Body body)
	   {
	    Local tmpString = Jimple.v().newLocal("tmpString1", RefType.v("java.lang.String"));
	    body.getLocals().add(tmpString);
	    return tmpString;
	   }

	 
	   private static Local addInfo(Body body,SootClass sClass)
	    {
		    
	        Local info = Jimple.v().newLocal("i"+ Integer.toString(id), sClass.getType());
	        id++;
	        body.getLocals().add(info);
	        return info;
	    }

	  static Local addTmpString2(Body body)
	   {
	    Local tmpString = Jimple.v().newLocal("tmpString2", RefType.v("java.lang.String"));
	    body.getLocals().add(tmpString);
	    return tmpString;
	   }

	 
}