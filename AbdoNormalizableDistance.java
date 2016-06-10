/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.NormalizableDistance;
import static weka.core.NormalizableDistance.R_MAX;
import static weka.core.NormalizableDistance.R_MIN;

/**
 *
 * @author Eng.Abdo
 */
public class AbdoNormalizableDistance extends NormalizableDistance {

    public AbdoNormalizableDistance() {
    }

    public AbdoNormalizableDistance(Instances data) {
        super(data);
    }

    @Override
    public String globalInfo() {

        return "Global Info From Eng.Abdo ";
    }

    @Override
    protected double updateDistance(double currDist, double diff) {
        
        double result;

        result = currDist;
        result += diff * diff;

        return result;
    }

    @Override
    public String getRevision() {
        return "Revision From Eng.Abdo";
    }

   public static int MinimumEditDistance(String word1,String word2)
    {
   int len1 = word1.length();
	int len2 = word2.length();
 
	// len1+1, len2+1, because finally return dp[len1][len2]
	int[][] dp = new int[len1 + 1][len2 + 1];
 
	for (int i = 0; i <= len1; i++) {
		dp[i][0] = i;
	}
 
	for (int j = 0; j <= len2; j++) {
		dp[0][j] = j;
	}
 
	//iterate though, and check last char
	for (int i = 0; i < len1; i++) {
		char c1 = word1.charAt(i);
		for (int j = 0; j < len2; j++) {
			char c2 = word2.charAt(j);
 
			//if last two chars equal
			if (c1 == c2) {
				//update dp value for +1 length
				dp[i + 1][j + 1] = dp[i][j];
			} else {
				int replace = dp[i][j] + 1;
				int insert = dp[i][j + 1] + 1;
				int delete = dp[i + 1][j] + 1;
 
				int min = replace > insert ? insert : replace;
				min = delete > min ? min : delete;
				dp[i + 1][j + 1] = min;
			}
		}
	}
 
	return dp[len1][len2];
    }

    public static int LCS(String x, String y) {

        int M = x.length();
        int N = y.length();

        // opt[i][j] = length of LCS of x[i..M] and y[j..N]
        int[][] opt = new int[M + 1][N + 1];

        // compute length of LCS and all subproblems via dynamic programming
        for (int i = M - 1; i >= 0; i--) {
            for (int j = N - 1; j >= 0; j--) {
                if (x.charAt(i) == y.charAt(j)) {
                    opt[i][j] = opt[i + 1][j + 1] + 1;
                } else {
                    opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
                }
            }
        }

        // recover LCS itself and print it to standard output
        int i = 0, j = 0;
        while (i < M && j < N) {
            if (x.charAt(i) == y.charAt(j)) {
                //System.out.print(x.charAt(i));
                i++;
                j++;
            } else if (opt[i + 1][j] >= opt[i][j + 1]) {
                i++;
            } else {
                j++;
            }
        }

        return opt[0][0];
    }

    @Override
    protected double difference(int index, double val1, double val2) {
        switch (m_Data.attribute(index).type()) {
            case Attribute.NOMINAL: {

                //System.out.println("Nominal From Eng.Abdo    " + m_Data.attribute(index).value(index));
                //System.out.println("Values are: val1 " + m_Data.attribute(index).value((int) val1) + "    val2 " + m_Data.attribute(index).value((int) val2));
                String path1 = m_Data.attribute(index).value((int) val1);
                String path2 = m_Data.attribute(index).value((int) val2);
                double dis= (1-(double)LCS(path1, path2)/Math.max(path1.length(),path2.length()));
//                System.out.println("dis is"+dis);
                return dis;
                //if (Instance.isMissingValue(val1) || Instance.isMissingValue(val2)
//                        || ((int) val1 != (int) val2)) {
//                    System.out.println("Here");
//
//                    return 1;
//                } else {
//                    return 0;
//                }
            }

            case Attribute.NUMERIC: {

                
                if (Instance.isMissingValue(val1) || Instance.isMissingValue(val2)) {
                    if (Instance.isMissingValue(val1) && Instance.isMissingValue(val2)) {
                        if (!m_DontNormalize) {
                            return 1;
                        } else {
                            return (m_Ranges[index][R_MAX] - m_Ranges[index][R_MIN]);
                        }
                    } else {
                        double diff;
                        if (Instance.isMissingValue(val2)) {
                            diff = (!m_DontNormalize) ? norm(val1, index) : val1;
                        } else {
                            diff = (!m_DontNormalize) ? norm(val2, index) : val2;
                        }
                        if (!m_DontNormalize && diff < 0.5) {
                            diff = 1.0 - diff;
                        } else if (m_DontNormalize) {
                            if ((m_Ranges[index][R_MAX] - diff) > (diff - m_Ranges[index][R_MIN])) {
                                return m_Ranges[index][R_MAX] - diff;
                            } else {
                                return diff - m_Ranges[index][R_MIN];
                            }
                        }
                        return diff;
                    }
                } else {
                    //System.out.println(m_Data.attribute(index).name()+"   "+ (norm(val1, index) - norm(val2, index)));
                    
                    return (!m_DontNormalize) ? (norm(val1, index) - norm(val2, index))
                            : (val1 - val2);
                }
            }

            default:
                return 0;
        }

    }
}
