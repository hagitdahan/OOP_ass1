public class ConcretePlayer implements Player{
	
	private boolean numPlayer;
	private int wins;
    public ConcretePlayer(boolean numplayer) {
		this.numPlayer=numplayer;
		this.wins = 0;	
	}
	//player one is the defender
    //true - defender
    @Override
    public boolean isPlayerOne() {
        return numPlayer;
    }

    public void setWins() {
    	this.wins=this.wins+1;
    }

    @Override
    public int getWins() {
        return this.wins;
    }

}
