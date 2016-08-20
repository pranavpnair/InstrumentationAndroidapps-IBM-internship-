import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;


public class TrySoot {

public static void setSootClassPath(String dir_class_files){
  String libPath = System.getProperty( "java.home" ) + File.separator + "lib"
            + File.separator + "rt.jar";
  String libPath1 = System.getProperty( "java.home" ) + File.separator + "lib"
            + File.separator + "jsse.jar";
  String libPath2 = System.getProperty( "java.home" ) + File.separator + "lib"
            + File.separator + "jce.jar";
 
  String l = libPath + ";" + libPath1 + ";" + libPath2;
  Scene.v().setSootClassPath(Scene.v().getSootClassPath()+";"+dir_class_files+";"+l+";C:/android-platforms-master/android-8/android.jar");
 
 }

public static void setSootClassPath(){
	  String libPath = System.getProperty( "java.home" ) + File.separator + "lib"
	            + File.separator + "rt.jar";
	  String libPath1 = System.getProperty( "java.home" ) + File.separator + "lib"
	            + File.separator + "jsse.jar";
	  String libPath2 = System.getProperty( "java.home" ) + File.separator + "lib"
	            + File.separator + "jce.jar";
	 
	  String l = libPath + ";" + libPath1 + ";" + libPath2;
	  Scene.v().setSootClassPath(Scene.v().getSootClassPath()+";"+l);
	 
	 }

  
  void driver(String dir_class_files) throws Exception{
 String[] args;
 List<String> argsList = new ArrayList<String>();
   argsList.addAll(Arrays.asList(new String[]{	
     "-pp",
   "-allow-phantom-refs",
    "-no-bodies-for-excluded",
     "-w",// for call graph
     "-process-dir",
     dir_class_files,
     "-p", "jb", "use-original-names",
     "-p", "cg", "all-reachable:true", // for call graph
     "-p", "cg", "implicit-entry:true", // for call graph	     
   }));
 args = argsList.toArray(new String[0]);
 
 //List<SootMethod> entryPoints = new ArrayList();	 	 

 soot.Main.main(args);
 	
 
 Iterator<SootClass> App_classes = Scene.v().getApplicationClasses().iterator();
 while(App_classes.hasNext()){
 SootClass c = App_classes.next();
      System.out.println(c.getName());
      }
  }

public static void main(String[] args) {
// TODO Auto-generated method stub
setSootClassPath("../TestJava/bin");
try {
new TrySoot().driver("../TestJava/bin");
} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}

}