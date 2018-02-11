public class Data
{

	private String date;
	private String station;
	private String program;
	private String systemStatus;
	private String processStatus;
	private String nogo;
	private String session;



	public Data(String date, String station, String program, String systemStatus, String processStatus, String nogo, String session)
	{
		this.date=date;
		this.station=station;
		this.program=program;
		this.systemStatus=systemStatus;
		this.processStatus=processStatus;
		this.nogo=nogo;
		this.session=session;
	}


	public String getDate()
	{
		return this.date;
	}
	public void setDate(String pDate)
	{
		date=pDate;
	}
	public String getStation()
	{
		return this.station;
	}
	public String getProgram()
	{
		return this.program;
	}
	public String getSystemStatus()
	{
		return this.systemStatus;
	}
	public String getProcessStatus()
	{
		return this.processStatus;
	}
	public String getNogo()
	{
		return this.nogo;
	}
	public String getSession()
	{
		return this.session;
	}

	@Override
	public String toString()
	{
		return date + " " + station + " " + program + " " + systemStatus + " " + processStatus + " " + nogo + " " + session; 
	}
}	