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
public class S extends Algorithm //This class implements the algorithm. Every algorithm will have to contain its specific implementation within the method "execute". The latter will contain a main loop performing the iterations, and will have to return the fitness trend (including the final best) solution. Look at this examples before implementing your first algorithm.
{
	@Override
	//to implement a different algorithm you'll have to change the content of this function
	public FTrend execute(Problem problem, int maxEvaluations) throws Exception
	{
		FTrend FT = new FTrend();
	
	    int problemDimension = problem.getDimension(); 
		double[][] bounds = problem.getBounds();

		// particle (the solution, i.e. "x")
		double[] x_best = new double[problemDimension];
		double x_fbest; //fitness value, i.e. "f(x)"
		int i = 0;
		// initial solution
		if (initialSolution != null)
		{
			x_best = initialSolution;
		    x_fbest = initialFitness;
		}
		else//random intitial guess
		{
			x_best = generateRandomSolution(bounds, problemDimension);//generating random value to start pointing the particle in search space
			x_fbest = problem.f(x_best);//applying random position problem and stored it as fittnes value
			FT.add(i, x_fbest);//storing calculated fitness value to the result 
			i++;
		}
			
		double alpha = getParameter("s0");//assigning alpha value
		int n = bounds.length;//initialising length of n for the loop
		boolean improved = false;// introducing boolean value as false
		double[] x_s = x_best;//storing initial position in new variable
		double[] delta = new double[problemDimension];//declaring size of the delta
		
		for(int j=0; j < n; j++) {
			delta[j] = alpha * (bounds[j][1] - bounds[j][0]);//formula to calculate delta
		}
		
		
		while(i < maxEvaluations) {
			improved = false;
			for (int j = 0; j < problemDimension && i < maxEvaluations; j++) {
				x_s[j] = x_best[j] - delta[j];//changing initial location by subtracting it with delta value
				x_s = toro(x_s, bounds);//using toro to maintain function not to go out of boundaries
				x_fbest = problem.f(x_s);//applying new position in problem
				i++;
				if(i%problemDimension==0)
					FT.add(i, x_fbest);
				//if new fitness value is less than old
				if(problem.f(x_s) <= problem.f(x_best)) {
					x_best[j] = x_s[j];//then storing it as best position
					improved = true;//making it true
		
				}
				//if new fitness value is not improved
				else {
					x_s[j] = x_best[j];//restoring intial value
				    x_s[j] = x_best[j] + delta[j]/2;// going half step in opposite direction
				    x_fbest = problem.f(x_s);//again computing with new value
					if(problem.f(x_s) <= problem.f(x_best))  {
				    	x_best[j] = x_s[j];//if improved store it as best value
				    	improved = true;
				  
				    }
				    else {
				    	x_s[j] = x_best[j];//still not improved storing old value as best
				        }
				    }
				
				}
			//still the system is not imporved
			if (improved != true) {
				for(int j=0; j < delta.length; j++) {
					delta[j] = delta[j]/2;//initialising delta by half value
				}
			}
			}
		finalBest = x_best; //storing final best
		FT.add(i, x_fbest);//adding it to the problem
		return FT;//returning it
			
		}

}

