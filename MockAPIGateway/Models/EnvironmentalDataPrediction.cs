using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MockAPIGateway.Models
{
    public class EnvironmentalDataPrediction
    {
        public string Date { get; set; }
        public double Temperature { get; set; }
        public double Humidity { get; set; }
        public double LightIntensity { get; set; }
    }
}
