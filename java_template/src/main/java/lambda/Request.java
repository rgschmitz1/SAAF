package lambda;

/**
 *
 * @author Wes Lloyd
 */
public class Request {
	private String bucketname, filename;
	private int row, col;
	String name;

	public String getBucketname() {
		return bucketname;
	}

	public String getFilename() {
		return filename;
	}

	public String getName() {
		return name;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public String getNameALLCAPS() {
		return name.toUpperCase();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setBucketname(String bucketname) {
		this.bucketname = bucketname;
	}

	public Request(String name) {
		this.name = name;
	}

	public Request() {

	}
}
