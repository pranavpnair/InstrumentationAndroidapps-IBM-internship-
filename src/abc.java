import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Modifier;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Type;
import soot.Unit;
import soot.JastAddJ.IfStmt;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;
import soot.options.Options;


public class abc {
	
	public static void main(String[] args) {
		String dir_class_files = "C:/Users/IBM_ADMIN/Desktop/coursera-android-master/Examples/ThreadingAsyncTask/bin/ThreadingAsyncTask.apk";
		TrySoot.setSootClassPath(dir_class_files);
		//-android-jars C:\android-platforms-master -process-dir C:\Users\IBM_ADMIN\Downloads\AndroidCalculator.apk
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
//		     "-f","J"
		   }));
		 args = argsList.toArray(new String[0]);
		 

	
		
		
		
		//prefer Android APK files// -src-prec apk
		Options.v().set_src_prec(Options.src_prec_apk);
		
		//output as APK, too//-f J
//		Options.v().set_output_format(Options.output_format_dex);
		
        // resolve the PrintStream and System soot-classes
		Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
        Scene.v().loadClassAndSupport("java.lang.Object");
        Scene.v().loadClassAndSupport("java.lang.System");
        SootClass sClass = new SootClass("Info", Modifier.PUBLIC);
        sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
        Scene.v().addClass(sClass);
       
        
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new BodyTransformer() {
        //	List<Type> types = new ArrayList<Type>();
        	
        	@Override
        	protected void internalTransform(Body b, String arg1,
        			Map<String, String> arg2) {
        		final PatchingChain<Unit> units = b.getUnits();
        		SootClass c = b.getMethod().getDeclaringClass();
        		//System.out.println(c.getName()+ " -> "+b.getMethod().getName());
        		if(c.hasSuperclass()){
        			SootClass scl = c.getSuperclass();
        			
        			if(scl.getName() == "android.os.AsyncTask"){
        				List<SootMethod> lt = scl.getMethods();
        			
        				//System.out.println("Param types " + b.getMethod().getParameterTypes());
        				
        				List<Type> doinbgd=null;
        		    	List<Type> onprgsupdate=null;
        		    	List<Type> onpostexec=null;
        				
        			for(int i=0;i< lt.size();i++)
        				{
        					SootMethod m= lt.get(i);
        					//System.out.println(m.getName());
        					if(m.getName().equals("doInBackground"))
        					{
        						doinbgd = m.getParameterTypes();
        					//	System.out.println(doinbgd);
        					}
        					else if(m.getName().equals("onProgressUpdate"))
        					{
        						onprgsupdate=m.getParameterTypes();
        					}
        					else if(m.getName().equals("onPostExecute"))
        					{
        						onpostexec=m.getParameterTypes();
        					}
        					
        				}
        			
        			
        				//b.getMethod().setParameterTypes(l);
        						
        				
        				
        				//important to use snapshotIterator here
        				for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
        					final Unit u = iter.next();
        					u.apply(new AbstractStmtSwitch() {
        						public void caseInvokeStmt(InvokeStmt stmt) {
        							InvokeExpr invokeExpr = stmt.getInvokeExpr();					
        							//String line= "extends AsyncTask";//methodname = null;
        							//invokeExpr.setArg( arg1);
        						}
        					});				
        				}				
        				
        				
        			}
        		}

        	}	
        }));
		
		soot.Main.main(args);
	}

    private static Local addTmpRef(Body body)
    {
        Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
        body.getLocals().add(tmpRef);
        return tmpRef;
    }
    
    private static Local addTmpString(Body body)
    {
        Local tmpString = Jimple.v().newLocal("tmpString", RefType.v("java.lang.String")); 
        body.getLocals().add(tmpString);
        return tmpString;
    }
}