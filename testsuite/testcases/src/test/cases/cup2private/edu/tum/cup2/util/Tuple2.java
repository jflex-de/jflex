package edu.tum.cup2.util;


/**
 * This class combines two instances of two given classes
 * to one instance.
 * 
 * This is especially useful for multiple return values.
 * Even more, tupel identity is defined semantically rather than
 * instance based - we combine the hash-values of e1 and e2 therefore.
 * 
 * @author Andreas Wenger
 * @author Michael Petter
 */
public class Tuple2<T1, T2>
{
	
	private final T1 e1;
	private final T2 e2;
        private int hashcache;
	
	
	public Tuple2(T1 e1, T2 e2)
	{
		this.e1 = e1;
		this.e2 = e2;
                this.hashcache = (e1.hashCode()+e2.hashCode()*97)/53;
	}
	
	
	public static <T1, T2> Tuple2<T1, T2> t(T1 e1, T2 e2)
	{
		return new Tuple2<T1, T2>(e1, e2);
	}
	
	
	public T1 get1()
	{
		return e1;
	}
	
	
	public T2 get2()
	{
		return e2;
	}

    @Override
    public int hashCode() {
        return hashcache;
    }

    @Override
    public String toString() {
        return "[ "+e1.toString()+" | "+e2.toString()+" ]";
    }
        

}
