
package javafxproject;


import java.io.File;
import java.net.URL;

import javafx.util.Duration;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

/**
 *
 * @author star
 */
public class FXMLDocumentController implements Initializable  {
    private String filepath;
    private MediaPlayer mediaplayer;
    private int flag;
    private int loopflag=0;
    private Duration duration;
    private Duration currentTime;
    private double rate=1;
    private double volflag=0;
    @FXML
    private Label label;
    
    @FXML
    private MediaView mediaView;
    
    @FXML
    private Slider slider;
    
    @FXML
    private Slider seek;
    
    @FXML
    private Label current,total;
    
    @FXML
    private ProgressBar pb;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
         FileChooser fileChooser=new FileChooser();
        FileChooser.ExtensionFilter filter=new FileChooser.ExtensionFilter("Select a file (*.mp3,*.mp4)","*.mp3","*.mp4");
        fileChooser.getExtensionFilters().add(filter);
        File file=fileChooser.showOpenDialog(null);
        filepath=file.toURI().toString();
        if(filepath!=null)
        {
            Media media = new Media(filepath);
            mediaplayer=new MediaPlayer(media);
            
            mediaView.setMediaPlayer(mediaplayer);
            String extension=getFileExtension(file);
            if(extension.equalsIgnoreCase("mp3"))
            {
                mediaplayer.play();
                
            }
            else
            {
            DoubleProperty width=mediaView.fitWidthProperty();
            DoubleProperty height=mediaView.fitHeightProperty();
            
            
            
            width.bind(Bindings.selectDouble(mediaView.sceneProperty(),"width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(),"height"));
            mediaView.setPreserveRatio(true);
            
            mediaplayer.play();
            }
            slider.setValue(mediaplayer.getVolume()*100);
            slider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaplayer.setVolume(slider.getValue()/100);
                }
            });
           mediaplayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    
                    currentTime=mediaplayer.getCurrentTime();
                    current.setText(formatTime(currentTime));
                    duration=mediaplayer.getTotalDuration();
                    total.setText(formatTimes(duration));
                    seek.setValue(newValue.toSeconds()*(100/duration.toSeconds()));
                }
            });
           seek.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaplayer.seek(Duration.seconds(seek.getValue()));
                }
            });
          
        }
    }
     public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
    @FXML
    private void playMedia(ActionEvent event){
        mediaplayer.play();
        rate=1;
        mediaplayer.setRate(rate);
    }
    
    @FXML
    private void pauseMedia(ActionEvent event){
        mediaplayer.pause();
    }
    
    @FXML
    private void stopMedia(ActionEvent event){
        mediaplayer.stop();
    }
    
   
    
    @FXML
    private void slowMedia(ActionEvent event){
        if(rate==0)
        {
            rate=rate+0.20;
        }
        rate=rate-0.20;
      mediaplayer.setRate(rate);
    }
        
    
    @FXML
    private void fastMedia(ActionEvent event){
       if(rate==2)
       {
           rate=rate-0.20;
       }
       rate=rate+0.20;
        mediaplayer.setRate(rate);
        
    }
    
     @FXML
    private void loopMedia(ActionEvent event){
        
        mediaplayer.setOnEndOfMedia(new Runnable() {
       public void run() {
           if(loopflag==0)
                {
                mediaplayer.seek(Duration.ZERO);
                }
           loopflag=1;
       }
   });
        loopflag=0;
        
    }
    
    @FXML
    private void muteButton(ActionEvent event)
    {
        if(volflag==0)
        {
            volflag=1;
            mediaplayer.setVolume(0);
        }
        else{
            volflag=0;
            mediaplayer.setVolume(1);
        }
        
    }
    
    
    @FXML
    private void exitMedia(ActionEvent event){
        System.exit((0));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    } 
     private String formatTimes(Duration duration) {
           

            if (duration.greaterThan(Duration.ZERO)) {
                int intDuration = (int)Math.floor(duration.toSeconds());
                int durationHours = intDuration / (60 * 60);
                if (durationHours > 0) {
                    intDuration -= durationHours * 60 * 60;
                }
                int durationMinutes = intDuration / 60;
                int durationSeconds = intDuration - (durationMinutes * 60);

                if (durationHours > 0) {
                    return String.format("%d:%02d:%02d",
                                         
                                         durationHours, durationMinutes, durationSeconds);
                } else {
                    return String.format("%02d:%02d",
                                         
                                         durationMinutes, durationSeconds);
                }
            } 
        return String.format("%02d:%02d", 0,0);
            
     }
      private String formatTime(Duration elapsed) {
            int intElapsed = (int)Math.floor(elapsed.toSeconds());
            int elapsedHours = intElapsed / (60 * 60);
            if (elapsedHours > 0) {
                intElapsed -= elapsedHours * 60 * 60;
            }
            int elapsedMinutes = intElapsed / 60;
            int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

            
                if (elapsedHours > 0) {
                    return String.format("%d:%02d:%02d",
                                         elapsedHours, elapsedMinutes, elapsedSeconds);
                } else {
                    return String.format("%02d:%02d",
                                         elapsedMinutes, elapsedSeconds);
                }
            }
        
    
    
    
}


