package STALGCMMP.GUI;

import java.awt.Color;
import java.util.ArrayList;

import STALGCMMP.PDA;
import STALGCMMP.Timeline;

public class Controller {
    PDA pda;
    MainView view;

    int status = 0;
    
    public Controller(PDA pda) {
        this.pda = pda;
        this.view = new MainView();

        setUpButtons();
    }
    
    private void setUpButtons(){
        view.inputButton.addActionListener(e -> {
            String input = view.inputArea.getText();
            if(input.equals("")) return;
            pda.init(input);
            view.inputLabel.setText("Input: " + pda.tape);
            view.statusLabel.setText("Status: Input set");

            ArrayList<LeafView> leafViews = new ArrayList<>();

            for(Timeline t: pda.timelines){
                leafViews.add(new LeafView(t.currState, pda.tape, t.stack, t.head));
            }

            view.updateTimelines(leafViews);
            status = 0;
        });

        view.resetButton.addActionListener(e -> {
            view.inputLabel.setText("Input: N/A");
            view.statusLabel.setText("Status: N/A");
            view.inputArea.setText("");
            view.updateTimelines(new ArrayList<>());
            status = 0;
        });

        view.lambda.addActionListener(e -> {
            view.inputArea.setText("ε");
        });

        view.nextButton.addActionListener(e -> {
            if(status != 1){
                status = pda.nextStep();
                if(status == 1){
                    view.statusLabel.setText("Status: Accepted");
                    ArrayList<LeafView> leafViews = new ArrayList<>();

                    for(Timeline t: pda.timelines){
                        leafViews.add(new LeafView(t.currState, pda.tape, t.stack, t.head));
                    }

                    view.updateTimelines(leafViews);

                    for(LeafView l: view.leafViews){
                        l.setBackground(Color.green);
                    }
                }else if(status == -1){
                    view.statusLabel.setText("Status: Rejected");

                    ArrayList<LeafView> leafViews = new ArrayList<>();

                    for(Timeline t: pda.timelines){
                        leafViews.add(new LeafView(t.currState, pda.tape, t.stack, t.head));
                    }

                    for(LeafView l: view.leafViews){
                        l.setBackground(Color.red);
                    }
                }else{
                    ArrayList<LeafView> leafViews = new ArrayList<>();

                    for(Timeline t: pda.timelines){
                        leafViews.add(new LeafView(t.currState, pda.tape, t.stack, t.head));
                    }

                    view.updateTimelines(leafViews);
                }
            }
        });
    }
}