/*
This are common utility functions for Java which it was necessary to develop for Neural Distributed Associative Memory project, 
	- It seems that converters for low level datatypes such as BitSets are not strongly support in Java . Functions such as:
		- Bitset to String
		- String to Biset
		- IntArray to String by a space delimiter
		- String to IntArray (assuming ints delimeted by a space)
		- String to IntArray delimeter defined at input
		- Biset to Bipolar
		- Integer to Bipolar
		
		
	- Includes other simple utilitarian functions such as 
		- Array Joins - by an input delimeter (Integer Array, List String Array) 
		- Position sum of two Int  Arrays bounded by upper/lower constraint
		- Hamming Distance over bitsets
		- Random Byte Generation for any input size n
		- Reduce Bipolar Array (from ints to 1 or -1)
		- Print a Bitset
		- Print a Bitset of any size
		
		
        
By Mina Rady
*/ 
 public String ArrayJoin (List <String> l, String del)
	{
		String res = "";
		for (int i =0;i<l.size();i++)
		{

			res += l.get(i);
			if (i!=l.size()-1)
				res+=del;
		}
		return res;
	}
	public String IntArrayJoin (int [] l, String del)
	{
		String res = "";
		for (int i =0;i<l.length;i++)
		{

			res += Integer.toString(l[i]);
			if (i!=l.length-1)
				res+=del;
		}
		return res;
	}

	public  int HammingDistance(BitSet x, BitSet y) {
		//System.out.println(this.X_size);
		//System.out.println(this.X_size);
		System.out.println("-----X------");
		PrintBits(x);
		System.out.println("-----END X------");
		System.out.println("-----Y------");
		PrintBits(y);
		System.out.println("-----END Y------");
       // if (x.size() != y.size())
         //   throw new IllegalArgumentException(String.format("BitSets have different length: x[%d], y[%d]", x.size(), y.size()));

        int dist = 0;
        for (int i = 0; i < this.X_size; i++) {
            if (x.get(i) != y.get(i))
                dist++;
        }

        return dist;
    }
	 public   byte[] GenRandomBytes(int size) {
		 	byte[] r = new byte[size]; //10000/4
			Random random = new Random();
			random.nextBytes(r);
			//System.out.println("old bits");
			//System.out.flush();
			//PrintBits(BitSet.valueOf(r));
	 	     return r;
	 	   }
	 public  String BitsToString (BitSet b)
	 {
		 String encoded = "";
		 byte [] bb = b.toByteArray();

		 /*for (int i = 0; i <b.length(); i++) {
	        encoded +=(b.get(i)?"1":"0");
	      }*/
		 encoded = DatatypeConverter.printBase64Binary(bb);
		 System.out.println("encoded string");

		 System.out.println(encoded);
		 return encoded;
	 }
	 public  BitSet StringToBits (int Size , String s64)
	 {
		 String encoded = "";
		 BitSet b = new BitSet (Size);
		 b= BitSet.valueOf(s64.getBytes());
		 System.out.println("decoded string");
		 PrintBits(b);
		/* for (int i = 0; i <b.length(); i++) {
	        encoded +=(b.get(i)?"1":"0");
	      }
	     
		 //encoded = DatatypeConverter.printBase64Binary(bb);
		 

		 System.out.println(encoded); */
		 return b;
	 }
	 public String IntArrayToString (int [] c)
	 {
		 String d = "";
		 for (int i =0;i<c.length;i++)
		 {
			d+= Integer.toString(c[i])+" ";
		 }
		 return d;
	 }
	 public int [] StringToIntArray (String c)
	 {
		 String [] vals = c.split(" ");
		 int []d = new int [vals.length];
		 for (int i =0;i<d.length;i++)
		 {
			 d[i]= Integer.parseInt(vals[i]);
		 }
		 return d;
	 }
	 public int [] StringToIntArrayFlex (String c, String del)
	 {
		 String [] vals = c.split(del);
		 int []d = new int [vals.length];
		 for (int i =0;i<d.length;i++)
		 {
			 d[i]= Integer.parseInt(vals[i]);
		 }
		 return d;
	 }
	 public PositionSumResult PositionSum(int [] a , int [] b, boolean constrained)
	 {
		System.out.println("Will Sum");
		System.out.println(Arrays.toString(a));
		System.out.println(Arrays.toString(b));
		PositionSumResult result =  new PositionSumResult (a.length);
		 int [] res = new int [a.length];
		 int sum;
		 boolean valid = true;
		 for (int i =0;i<a.length;i++)
		 {
			 sum =a[i]+b[i];
			 if (!constrained || (sum<= this.C && sum >=this.C*-1))
			 {
				 res [i]=sum;
			 }else {
				 result.valid=false;
				 result.msg="Storage Sum Exceeded";
				 return result;
				 
			 }
		 }
		 System.out.println("Sum result");
		 System.out.println(Arrays.toString(result.result));
		 result.valid=true;
		 result.result= res;
		 return result;
	 }
	 public BitSet BipolarToBin (int [] t)
	 {
		 BitSet B = new BitSet(t.length);
		 for (int i =0;i<t.length;i++)
		 {
			 if (t[i]==1)
				 B.set(i);
		 }
		 return B;
	 }
	 public  int [] BinToBipolar (BitSet b)
	 {
		System.out.println("BinToBipolar");
		System.out.println(X_size);
		int [] res = new int [ this.X_size];
		System.out.println(res.length);
		for (int i = 0; i < this.X_size; i++) {
		        res[i] =(b.get(i)? 1:-1);
		      }		 
		 return res;
	 }
	 public int [] ReduceBipolar(int []b )
	 {
		 int [] result = new int [b.length];
		 for (int i =0;i<b.length;i++)
		 {
			 result[i]=(b[i]>=0?1:-1);
		 }
		 return result;
	 }
	 public  void PrintBits (BitSet b)
	 {
		 String encoded ="";
		 for (int i = 0; i <this.X_size; i++) {
		        encoded +=(b.get(i)?"1":"0");
		      }
			System.out.flush();

		 System.out.println(encoded);
		 System.out.flush();

	 }
	 public  void PrintBitsFlexible (BitSet b,int size)
	 {
		 String encoded ="";
		 for (int i = 0; i <size; i++) {
		        encoded +=(b.get(i)?"1":"0");
		      }
			System.out.flush();

		 System.out.println(encoded);
		 System.out.flush();

	 }