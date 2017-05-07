
public class OlsrNachricht {
	
	protected int hopCount;
	
	public boolean decHopCount(){
		if(this.hopCount > 0){
			this.hopCount--;
			return true;
		}
		return false;
	}
}
