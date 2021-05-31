package algorithms;
//in this part you can import the functionalities that yuo need to use for implementing your algorithm
import static utils.algorithms.Misc.*;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
/**
 * Intelligent Single Particle Optimization
 */
public class PSO extends Algorithm //This class implements the algorithm. Every algorithm will have to contain its specific implementation within the method "execute". The latter will contain a main loop performing the iterations, and will have to return the fitness trend (including the final best) solution. Look at this examples before implementing your first algorithm.
{
	

	@Override
	//to implement a different algorithm you'll have to change the content of this function
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		// first, we need to define variables for storing the paramters of the algorithm
//		double A = getParameter("p0");							
//		double P = getParameter("p1");							
//		int B = this.getParameter("p2").intValue();			
//		int S = this.getParameter("p3").intValue();		
//		double E = this.getParameter("p4");					
//		int PartLoop = this.getParameter("p5").intValue();	
		
		int NP = getParameter("b0").intValue();
		double c1 = getParameter("b1");
		double c2 = getParameter("b2");
//		double w = getParameter("b3");
		
		//we always need an object of the kynd FTrend (for storing the fitness trend), and variables for storing the dimesionality vlue and the bounds of the problem as showed below
		FTrend FT = new FTrend();
		int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// particle (the solution, i.e. "x")
//		double[][] particle = new double[NP][problemDimension];
//		double fParticle = Double.NaN; //fitness value, i.e. "f(x)"
		double rb = Math.random();
		double rg = Math.random();
		int i=0;

		double[][] s = new double[NP][problemDimension];
		double[][] v = new double[NP][problemDimension];
		double[][] x = new double[NP][problemDimension];
		double[] xlb = new double[NP];
		double[] xgb = new double[problemDimension];
	    double new_best = Double.NaN;
        double x_best = Double.NaN;
	    double phi1 = random(0, c1);
	    double phi2 = random(0, c2);
	    double phi3 = 0.5 + Math.random()/3;
		for(int j=0; j<NP; j++) {
			double[] particle = generateRandomSolution(bounds, problemDimension);
			for(int k=0; k<problemDimension; k++) {
				s[j][k] = particle[k];
				v[j][k] = 0.1 * s[j][k];
				x[j][k] = s[j][k] + v[j][k];
				}
			xlb[j] = problem.f(s[j]);
			if(j==0 || xlb[j] < x_best) {
				x_best = xlb[j];
				for(int k=0; k<problemDimension; k++) {
					xgb[k] = s[j][k];
					FT.add(i, x_best);
				}
			}
			i++;
		}
		while(i < maxEvaluations) {
			for(int n=0; n< NP; n++) {
				for(int m=0; m<problemDimension && i<maxEvaluations; m++) {
					v[n][m] = phi3 * v[n][m] + phi1 * rb * (x[n][m] - x[n][m]) + phi2 * rg * (xgb[m] - x[n][m]);
					x[n][m] += v[n][m];
				    s[n][m] = x[n][m];
				}
				s[n] = toro(s[n], bounds);
				new_best = problem.f(s[n]);
				i++;
				if(new_best < xlb[n]) {
					xlb[n] = new_best;
					if(new_best < x_best){
						x_best = new_best;
					    for(int m=0; m< problemDimension; m++) {
					    	xgb[m] = s[n][m];
					    FT.add(i, new_best);
						}
				}
				}
			i++;
			}
		}
		finalBest = xgb; //storing final best
		FT.add(i, new_best);//adding it to the problem
		return FT;//returning it
	}
}



