package algorithms;
//in this part you can import the functionalities that yuo need to use for implementing your algorithm
import static utils.algorithms.Misc.generateRandomSolution;
import static utils.algorithms.Misc.toro;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore.FTrend;
	/**
	 * Intelligent Single Particle Optimization
	 */
public class MA extends Algorithm //This class implements the algorithm. Every algorithm will have to contain its specific implementation within the method "execute". The latter will contain a main loop performing the iterations, and will have to return the fitness trend (including the final best) solution. Look at this examples before implementing your first algorithm.
{
	@Override
	//to implement a different algorithm you'll have to change the content of this function
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		FTrend FT = new FTrend();
		
		int NP = getParameter("m0").intValue();
		double alpha = getParameter("m1");
	    int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// particle (the solution, i.e. "x")
		double[][] x_best = new double[NP][problemDimension];
		double x_fbest = Double.NaN; //fitness value, i.e. "f(x)"
		int i = 0;
		// initial solution
		if (initialSolution != null)
		{
			for(int j=0; j<NP; j++) {
				x_best[j] = initialSolution;
			    x_fbest = initialFitness;
			}
			
		}
		else//random intitial guess
		{
			for(int j=0; j<NP; j++) {
				x_best[j] = generateRandomSolution(bounds, problemDimension);//generating random value to start pointing the particle in search space
				x_fbest = problem.f(x_best[j]);//applying random position problem and stored it as fittnes value
				FT.add(i, x_fbest);//storing calculated fitness value to the result 
				i++;
			}
			
		}
			
		boolean improved = false;
		double[][] x_s = new double[NP][problemDimension];
		double[] delta = new double[problemDimension];//declaring size of the delta
		double[] new_best = new double[problemDimension];
		for(int j=0; j < problemDimension; j++) {
			delta[j] = alpha * (bounds[j][1] - bounds[j][0]);//formula to calculate delta
		}
		
		
		while(i < maxEvaluations) {
			improved = false;
			for(int j=0; j<NP; j++) {
				for (int k = 0; k < problemDimension; k++) {
					x_s[j][k] = x_best[j][k] - delta[k];
					x_s[j] = toro(x_s[j], bounds);
					x_fbest = problem.f(x_s[j]);
					i++;
				}
				if(i%problemDimension==0)
					FT.add(i, x_fbest);
				if(problem.f(x_s[j]) < problem.f(x_best[j])) {
					x_best[j] = x_s[j];//then storing it as best position
					improved = true;//making it true
					}
				//if new fitness value is not improved
				else {
					for(int m=0; m<problemDimension; m++) {
						x_s[j][m] = x_best[j][m];//restoring intial value
					    x_s[j][m] = x_best[j][m] + delta[m]/2;// going half step in opposite direction
					    x_fbest = problem.f(x_s[j]);//again computing with new value
						if(problem.f(x_s[j]) < problem.f(x_best[j]))  {
					    	x_best[j][m] = x_s[j][m];//if improved store it as best value
					    	new_best[m] = x_best[j][m]; 
					    	improved = true;
					  
					    }
					    else {
					    	x_s[j][m] = x_best[j][m];//still not improved storing old value as best
					        }
					}
				    }
				
				}
			//still the system is not imporved
			if (improved != true) {
				for(int k=0; k < delta.length; k++) {
					delta[k] = delta[k]/2;//initialising delta by half value
				}
			}
			}
		
			
		finalBest = new_best; //storing final best
		FT.add(i, x_fbest);//adding it to the problem
		return FT;//returning it

	}
}

