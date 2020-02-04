using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Channels;
using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;
using MockAPIGateway.Models;

namespace MockAPIGateway.Hubs
{
    public class VehicleDataHub : Hub
    {
        public ChannelReader<TemperatureData> StreamTemperatureData(int vehicleId)
        {
            var channel = Channel.CreateUnbounded<TemperatureData>();

            _ = WriteTemperatureDataStream(channel.Writer);

            return channel.Reader;
        }

        private async Task WriteTemperatureDataStream(ChannelWriter<TemperatureData> writer)
        {
            for (var i = 0; i < 10; i++)
            {
                await writer.WriteAsync(new TemperatureData() { Temperature = 23.0 + i });
                await Task.Delay(500);
            }

            writer.TryComplete();
        }
        
        public ChannelReader<HumidityData> StreamHumidityData(int vehicleId)
        {
            var channel = Channel.CreateUnbounded<HumidityData>();

            _ = WriteHumidityDataStream(channel.Writer);

            return channel.Reader;
        }

        private async Task WriteHumidityDataStream(ChannelWriter<HumidityData> writer)
        {
            for (var i = 0; i < 10; i++)
            {
                await writer.WriteAsync(new HumidityData() { Humidity = 52.0 + i });
                await Task.Delay(500);
            }

            writer.TryComplete();
        }

        public ChannelReader<LightIntensityData> StreamLightIntensityData(int vehicleId)
        {
            var channel = Channel.CreateUnbounded<LightIntensityData>();

            _ = WriteLightIntensityDataStream(channel.Writer);

            return channel.Reader;
        }

        private async Task WriteLightIntensityDataStream(ChannelWriter<LightIntensityData> writer)
        {
            for (var i = 0; i < 10; i++)
            {
                await writer.WriteAsync(new LightIntensityData() { LightIntensity = 80.0 + i });
                await Task.Delay(500);
            }

            writer.TryComplete();
        }

        public ChannelReader<EnvironmentalDataPrediction> PredictEnvironmentalData(int vehicleId, string predictUntil, string time)
        {
            var channel = Channel.CreateUnbounded<EnvironmentalDataPrediction>();

            _ = WriteEnvironmentalDataPredictionStream(channel.Writer);

            return channel.Reader;
        }

        private async Task WriteEnvironmentalDataPredictionStream(ChannelWriter<EnvironmentalDataPrediction> writer)
        {
            for (var i = 0; i < 10; i++)
            {
                await writer.WriteAsync(new EnvironmentalDataPrediction()
                {
                    Date = "9.12.2018",
                    Temperature = 23.0 + i,
                    Humidity = 52.0 + i,
                    LightIntensity = 80.0 + i
                });
                await Task.Delay(500);
            }
        }
    }
}
