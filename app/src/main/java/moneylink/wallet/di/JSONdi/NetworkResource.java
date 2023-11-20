package moneylink.wallet.di.JSONdi;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;


/**
 * this is main generic call that will handle all response
 * form server and we can change its type in runtime
 * With the class can handle success error loading and unsuccess methods
 * but Currently handling the error and success
 * error will call on failure we api failed to called
 *
 * @param <T>
 */

public class NetworkResource<T> {


    /*status that we will set with error or success*/
    @NonNull
    public final Status status;

    /*will store message in it*/
    public final int messageResourceId;

    /*will set result with return type in this generic varaible it will chage itself auto*/
    @Nullable
    public final T resource;


    private NetworkResource(@Nullable T resource, int message, @NonNull Status status) {
        this.messageResourceId = message;
        this.resource = resource;
        this.status = status;
    }


    /*we use this method on success if there will be some response from
     * api is it if data come of just add it here other wise we make some change in specfic '
     * class and add it in */

    public static <T> NetworkResource<T> success(@Nullable T data) {
        return new NetworkResource<>(data, -1, Status.SUCCESS);
    }

    /*use this method on server error or some error we face on calling apis*/

    public static <T> NetworkResource<T> error(int messageId, @Nullable T data) {
        return new NetworkResource<>(data, messageId, Status.ERROR);
    }

    public static <T> NetworkResource<T> loading(@Nullable T data) {
        return new NetworkResource<>(data, -1, Status.LOADING);
    }

    public static <T> NetworkResource<T> unSuccess(int code, @Nullable T data){
        return new NetworkResource<>(data, code , Status.UNSUCCESS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NetworkResource<?> resource = (NetworkResource<?>) o;

        if (status != resource.status) {
            return false;
        }
        if (messageResourceId != -1 ? messageResourceId != resource.messageResourceId : resource.messageResourceId != -1) {
            return false;
        }
        return Objects.equals(this.resource, resource.resource);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (messageResourceId != 1 ? messageResourceId : 0);
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + messageResourceId + '\'' +
                ", resource=" + resource +
                '}';
    }

}

