package core.util;

import java.util.ArrayList;
import java.util.List;

import core.log.LogService;

public class MathUtils {
	 /// <summary>
    /// 自定义概率，随机返回传入值
    /// </summary>
    /// <param name="probability">传入概率的数组，所有值相加要等于100，如[60,30,10]</param>
    /// <param name="value">与概率数组个数相同的数组，根据随机返回对应的值</param>
    /// <returns></returns>
    public static Object ProbabilityRandom(int[] probability, Object[] value)
    {
        int r = RandomUtils.Range(0, 100);
        int low = 0;
        for (int i = 0; i < probability.length; i++)
        {

            if (i > 0)
                low += probability[i - 1];
            int top = low + probability[i];

            //Debug.Log("low :" + low + " top :" + top + " r :" + r);
            if (r >= low && r < top)
                return value[i];
        }

        return null;

    }
    public static <T> T ProbabilityRandom(List<Integer> probability, List<T> value)
    {
        int r = RandomUtils.Range(0, 100);
        int low = 0;
        for (int i = 0; i < probability.size(); i++)
        {

            if (i > 0)
                low += probability.get( i - 1);
            int top = low + probability.get(i);

            //Debug.Log("low :" + low + " top :" + top + " r :" + r);
            if (r >= low && r < top)
                return  value.get(i);
        }

        return null;

    }
    //安装权重随机数据（随机到的数据是否从List里移除）(returnRandomListIndex: 返回第一次随机的返回的Index)
    public static <T> T ProbabilityRandomListValue(List<Integer> probability, List<ArrayList<T>> value,boolean isReturnRemoveFromList,Integer returnRandomListIndex) throws Exception
    {
    	if (probability.size()!=value.size())
    	{
    		throw new Exception("ProbabilityRandomListValue 概率和值数目不同");
		}
        for (int i = 0; i < value.size(); i++)
        {
        	List<T> tempList= value.get(i);
			if (tempList.size()<=0)
			{
				value.remove(i);
				probability.remove(i);
				i--;
			}
		}
        
        if (value.size()==0)
        {
			return null;
		}
        
       List<T> list=  WeightProbabilityRandom(probability, value);
       returnRandomListIndex = value.indexOf(list);
       
       int r = RandomUtils.Range(0, list.size());
       T t =list.get(r);
       if (isReturnRemoveFromList)
       {
    	   list.remove(t);
       }
        return t;

    }
    /// <summary>
    /// 自定义概率，随机返回传入值
    /// </summary>
    /// <param name="probability">传入概率的数组，所有值相加要等于100，如[60,30,10]</param>
    /// <param name="value">与概率数组个数相同的数组，根据随机返回对应的值</param>
    /// <returns></returns>
    public static Object ProbabilityRandom(FixRandom random, int[] probability, Object[] value)
    {
        int r = random.Range(0, 100);
        int low = 0;
        for (int i = 0; i < probability.length; i++)
        {

            if (i > 0)
                low += probability[i - 1];
            int top = low + probability[i];

            //Debug.Log("low :" + low + " top :" + top + " r :" + r);
            if (r >= low && r < top)
                return value[i];
        }

        return null;

    }
    
	 /// <summary>
    /// 自定义权重，随机返回传入值
    /// </summary>
    /// <param name="probability">传入权重的数组，所有值相加要大于0，如[60,30,10]</param>
    /// <param name="value">与权重数组个数相同的数组，根据随机返回对应的值</param>
    /// <returns></returns>
    public static Object WeightProbabilityRandom(int[] probability, Object[] value)
    {
    	int max=0;
    	 for (int i = 0; i < probability.length; i++)
         {
    		 max+=probability[i];
         }
    	
        int r = RandomUtils.Range(0, max);
        int low = 0;
        for (int i = 0; i < probability.length; i++)
        {

            if (i > 0)
                low += probability[i - 1];
            int top = low + probability[i];

            //Debug.Log("low :" + low + " top :" + top + " r :" + r);
            if (r >= low && r < top)
                return value[i];
        }

        return null;

    }
	 /// <summary>
    /// 自定义权重，随机返回传入值
    /// </summary>
    /// <param name="probability">传入权重的数组，所有值相加要大于0，如[60,30,10]</param>
    /// <param name="value">与权重数组个数相同的数组，根据随机返回对应的值</param>
    /// <returns></returns>
    public static <T> T WeightProbabilityRandom(FixRandom random,List<Integer> probability, List<T> value)
    {
    	int max=0;
    	 for (int i = 0; i < probability.size(); i++)
         {
    		 max+=probability.get(i);
         }
    	
        int r = random.Range(0, max);
        int low = 0;
        for (int i = 0; i < probability.size(); i++)
        {

            if (i > 0)
                low += probability.get(i-1);
            int top = low + probability.get(i);

            //Debug.Log("low :" + low + " top :" + top + " r :" + r);
            if (r >= low && r < top)
                return  value.get(i);
        }

        return null;

    }
    /// <summary>
    /// 自定义权重，随机返回传入值
    /// </summary>
    /// <param name="probability">传入权重的数组，所有值相加要大于0，如[60,30,10]</param>
    /// <param name="value">与权重数组个数相同的数组，根据随机返回对应的值</param>
    /// <returns></returns>
    public static <T> T WeightProbabilityRandom(List<Integer> probability, List<T> value)
    {
    	if (probability.size() != value.size())
    	{
			LogService.Error("", "MathUtils.WeightProbabilityRandom 参数错误：参数个数不相等！"+probability.size()+" : "+value.size());
			return null;
		}
    	int max=0;
    	 for (int i = 0; i < probability.size(); i++)
         {
    		 max+=probability.get(i);
         }
    	
        int r = RandomUtils.Range(0, max);
        int low = 0;
        for (int i = 0; i < probability.size(); i++)
        {

            if (i > 0)
                low += probability.get(i-1);
            int top = low + probability.get(i);

            //Debug.Log("low :" + low + " top :" + top + " r :" + r);
            if (r >= low && r < top)
                return  value.get(i);
        }

        return null;

    }
    
  
}
