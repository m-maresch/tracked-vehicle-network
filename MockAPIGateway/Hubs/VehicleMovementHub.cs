using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;

namespace MockAPIGateway.Hubs
{
    public class VehicleMovementHub : Hub
    {
        public void RotateVehicleLeft(int vehicleId, int value)
        {

        }

        public void RotateVehicleRight(int vehicleId, int value)
        {

        }

        public void MoveVehicleForward(int vehicleId, int value)
        {

        }
    }
}
