package net.ncplanner.plannerator.planner;
public class MathUtil{
    public static String percent(double n, int digits){
        double fac = pow(10, digits);
        double d = (round(n*fac*100)/(double)round(fac));
        return (digits==0?round(d):d)+"%";
    }
    public static String round(double n, int digits){
        double fac = pow(10, digits);
        double d = round(n*fac)/(double)round(fac);
        return (digits==0?round(d):d)+"";
    }
    public static double pow(double a, double b){
        return com.codename1.util.MathUtil.pow(a, b);
    }
    public static double exp(double a){
        return com.codename1.util.MathUtil.exp(a);
    }
    public static long round(double d){
        return Math.round(d);
    }
    public static int round(float a){
        return Math.round(a);
    }
    public static double max(double a, double b){
        return Math.max(a, b);
    }
    public static float max(float a, float b){
        return Math.max(a, b);
    }
    public static int max(int a, int b){
        return Math.max(a, b);
    }
    public static long max(long a, long b){
        return Math.max(a, b);
    }
    public static double min(double a, double b){
        return Math.min(a, b);
    }
    public static float min(float a, float b){
        return Math.min(a, b);
    }
    public static int min(int a, int b){
        return Math.min(a, b);
    }
    public static long min(long a, long b){
        return Math.min(a, b);
    }
    public static double pi(){
        return Math.PI;
    }
    public static double toRadians(double degrees){
        return Math.toRadians(degrees);
    }
    public static double toDegrees(double radians){
        return Math.toDegrees(radians);
    }
    public static double sin(double a){
        return Math.sin(a);
    }
    public static double cos(double a){
        return Math.cos(a);
    }
    public static double log(double a){
        return com.codename1.util.MathUtil.log(a);
    }
    public static int logBase(int base, int n){
        return (int)(com.codename1.util.MathUtil.log(n)/com.codename1.util.MathUtil.log(base));
    }
    public static double getValueBetweenTwoValues(double pos1, double val1, double pos2, double val2, double pos){
        if(pos1>pos2){
            return getValueBetweenTwoValues(pos2, val2, pos1, val1, pos);
        }
        double posDiff = pos2-pos1;
        double percent = pos/posDiff;
        double valDiff = val2-val1;
        return percent*valDiff+val1;
    }
    public static float getValueBetweenTwoValues(float pos1, float val1, float pos2, float val2, float pos){
        if(pos1>pos2){
            return getValueBetweenTwoValues(pos2, val2, pos1, val1, pos);
        }
        float posDiff = pos2-pos1;
        float percent = pos/posDiff;
        float valDiff = val2-val1;
        return percent*valDiff+val1;
    }
    public static long nanoTime(){
        return System.currentTimeMillis()*1_000_000;
    }
    public static boolean isPrime(int n){
        if(n<=1)return false;for(int i = 2; i<n; i++){
            if(n%i==0)return false;
        }
        return true;
    }
    public static int nextPrime(int n, int step){
        if(n<=1)return 2;
        if(isPrime(n+step))return n+step;
        return nextPrime(n+step, step);
    }
}