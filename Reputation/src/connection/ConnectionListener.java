package connection;

public interface ConnectionListener<ResponseType> {
    
    public void onRequestComplete(ResponseType response);

    public void onError(int status, String error);
}