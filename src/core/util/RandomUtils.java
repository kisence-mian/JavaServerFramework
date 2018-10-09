package core.util;

public class RandomUtils {
	
	public static int Range(int min,int max)
	{
		int num=0;
		if(max<=min)
			num = min;
		else
		num =min+(int)(Math.random()*(max -min));
		
		return num;
	}
	public static boolean RandomYesNo()
	{
		int res=Range(0, 2);
		return res==1;
	}
	
	public static int RWS(int[] p)
	{
		int sum = 0;
		for (int i = 0; i < p.length; i++) {
			sum += p[i];
		}
		
		int tmp = 0;
		int random = Range(0, sum);
		
		for (int i = 0; i < p.length; i++) {
			tmp += p[i];
			if(tmp >= random)
			{
				return i;
			}
		}
		
		return p.length - 1;
	}
	//随机n%概率，是否随机到
	//probability(0-100)
	  public static boolean ProbabilityRandomYesNo(int probability)
	    {
	        int r = RandomUtils.Range(1, 101);
	       if (probability>100)
	       {
	    	   probability=100;
	       }
	       else  if (probability<0) 
	       {
			probability =0;
	       }
			
		return r<=probability;

	    }
}
