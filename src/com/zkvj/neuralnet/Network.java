package com.zkvj.neuralnet;


public class Network
{
   public Network(int[] aLayerSizes)
   {
      _numLayers = aLayerSizes.length;
      _layerSizes = aLayerSizes;
      
      initializeWeightsAndBiases();
   }

   private void initializeWeightsAndBiases()
   {
      if(_numLayers < 2)
      {
         System.out.println("ERROR: Network must have at least 2 layers");
      }
      
   }

   private final int _numLayers;
   private final int[] _layerSizes;
   
}
