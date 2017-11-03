package com.dbbest.a500px.loader.custom;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PictureDownloadRunnable implements Runnable {

    static final int HTTP_STATE_FAILED = -1;
    static final int HTTP_STATE_STARTED = 0;
    static final int HTTP_STATE_COMPLETED = 1;
    private static final int READ_SIZE = 1024 * 2;

    private final TaskRunnableDownloadMethods pictureTask;

    public PictureDownloadRunnable(TaskRunnableDownloadMethods task) {
        this.pictureTask = task;
    }

    @Override
    public void run() {
        pictureTask.setDownloadThread(Thread.currentThread());
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        byte[] byteBuffer = pictureTask.getByteBuffer();

        try {
            if (Thread.interrupted()) {

                throw new InterruptedException();

            }
            if (null == byteBuffer) {

                pictureTask.handleDownloadState(HTTP_STATE_STARTED);


                InputStream byteStream = null;
                try {

                    HttpURLConnection httpConn = (HttpURLConnection) pictureTask.getPictureURL().openConnection();
                    if (Thread.interrupted()) {

                        throw new InterruptedException();
                    }
                    byteStream = httpConn.getInputStream();

                    if (Thread.interrupted()) {

                        throw new InterruptedException();
                    }
                    int contentSize = httpConn.getContentLength();

                    if (-1 == contentSize) {

                        byte[] tempBuffer = new byte[READ_SIZE];

                        int bufferLeft = tempBuffer.length;
                        int bufferOffset = 0;
                        int readResult = 0;

                        /*
                         * The "outer" loop continues until all the bytes
                         * have been downloaded. The inner loop continues
                         * until the temporary buffer is full, and then
                         * allocates more buffer space.
                         */
                        outer:
                        do {
                            while (bufferLeft > 0) {

                                /*
                                 * Reads from the URL location into
                                 * the temporary buffer, starting at the
                                 * next available free byte and reading as
                                 * many bytes as are available in the
                                 * buffer.
                                 */
                                readResult = byteStream.read(tempBuffer, bufferOffset,
                                        bufferLeft);

                                /*
                                 * InputStream.read() returns zero when the
                                 * file has been completely read.
                                 */
                                if (readResult < 0) {
                                    // The read is finished, so this breaks
                                    // the to "outer" loop
                                    break outer;
                                }

                                /*
                                 * The read isn't finished. This sets the
                                 * next available open position in the
                                 * buffer (the buffer index is 0-based).
                                 */
                                bufferOffset += readResult;

                                // Subtracts the number of bytes read from
                                // the amount of buffer left
                                bufferLeft -= readResult;

                                if (Thread.interrupted()) {

                                    throw new InterruptedException();
                                }
                            }
                            /*
                             * The temporary buffer is full, so the
                             * following code creates a new buffer that can
                             * contain the existing contents plus the next
                             * read cycle.
                             */

                            // Resets the amount of buffer left to be the
                            // max buffer size
                            bufferLeft = READ_SIZE;

                            /*
                             * Sets a new size that can contain the existing
                             * buffer's contents plus space for the next
                             * read cycle.
                             */
                            int newSize = tempBuffer.length + READ_SIZE;

                            /*
                             * Creates a new temporary buffer, moves the
                             * contents of the old temporary buffer into it,
                             * and then points the temporary buffer variable
                             * to the new buffer.
                             */
                            byte[] expandedBuffer = new byte[newSize];
                            System.arraycopy(tempBuffer, 0, expandedBuffer, 0,
                                    tempBuffer.length);
                            tempBuffer = expandedBuffer;
                        } while (true);

                        /*
                         * When the entire image has been read, this creates
                         * a permanent byte buffer with the same size as
                         * the number of used bytes in the temporary buffer
                         * (equal to the next open byte, because tempBuffer
                         * is 0=based).
                         */
                        byteBuffer = new byte[bufferOffset];

                        // Copies the temporary buffer to the image buffer
                        System.arraycopy(tempBuffer, 0, byteBuffer, 0, bufferOffset);

                        /*
                         * The download size is available, so this creates a
                         * permanent buffer of that length.
                         */
                    } else {
                        byteBuffer = new byte[contentSize];

                        // How much of the buffer still remains empty
                        int remainingLength = contentSize;

                        // The next open space in the buffer
                        int bufferOffset = 0;

                        /*
                         * Reads into the buffer until the number of bytes
                         * equal to the length of the buffer (the size of
                         * the image) have been read.
                         */
                        while (remainingLength > 0) {
                            int readResult = byteStream.read(
                                    byteBuffer,
                                    bufferOffset,
                                    remainingLength);
                            /*
                             * EOF should not occur, because the loop should
                             * read the exact # of bytes in the image
                             */
                            if (readResult < 0) {

                                // Throws an EOF Exception
                                throw new EOFException();
                            }

                            // Moves the buffer offset to the next open byte
                            bufferOffset += readResult;

                            // Subtracts the # of bytes read from the
                            // remaining length
                            remainingLength -= readResult;

                            if (Thread.interrupted()) {

                                throw new InterruptedException();
                            }
                        }
                    }

                    if (Thread.interrupted()) {

                        throw new InterruptedException();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } finally {
                    if (null != byteStream) {
                        try {
                            byteStream.close();
                        } catch (Exception e) {
                            // Do nothing
                        }
                    }
                }
            }
            pictureTask.setByteBuffer(byteBuffer);
            pictureTask.handleDownloadState(HTTP_STATE_COMPLETED);
        } catch (InterruptedException e1) {
            // Do nothing
        } finally {
            if (null == byteBuffer) {
                pictureTask.handleDownloadState(HTTP_STATE_FAILED);
                pictureTask.setDownloadThread(null);
                Thread.interrupted();
            }
        }

    }

    public interface TaskRunnableDownloadMethods {

        void setDownloadThread(Thread currentThread);

        byte[] getByteBuffer();

        void setByteBuffer(byte[] buffer);

        void handleDownloadState(int state);

        URL getPictureURL();
    }
}
